import React, { useCallback, useContext, useEffect, useState } from "react";
import "./PatientTabs.css";
import { Tab, Tabs } from "react-bootstrap";
import dayjs from "dayjs";
import { Link } from "react-router-dom";
import PrescriptionItems from "../PrecriptionItems/PrecriptionItems";
import { authAPI, endpoints } from "../config/Api";
import { Alert } from "@mui/material";
import { UserContext } from "../config/Context";
import { isBACSI, isBENHNHAN } from "../Common/Common";

export default function PatientTabs({
  examPatient,
  setExamPatient,
  historyExamsPatient,
  setHistoryExamPatient,
  getHistoryUserRegister,
  historyPaymentPatient,
  setHistoryPaymentPatient,
  getPaymentHistoryPatientByName,
}) {
  // không thể để state của historyExamsPatient ở đây đc , vì nó thay đổi mỗi component con 
  //, mà component cha đang chứa giao diện thằng này , dẫn đến ko đổi
  const [selectMedicalExamId, setSelectMedicalExamId] = useState(0);
  const [precriptionItems, setPrecriptionItems] = useState([]);

  const { currentUser } = useContext(UserContext);

  const getPrescriptionItemsByMedicalExamId = async (selectMedicalExamId) => {
    try {
      const response = await authAPI().get(
        endpoints["getPrescriptionItemsByMedicalExamId"](selectMedicalExamId),
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );
      if (response.status === 200) {
        setPrecriptionItems(response.data);
        console.log("Thành công", response.data);
      } else console.log("Lỗi", response.data);
    } catch {
      console.log("Lỗi");
    }
  };

  return (
    <>
      <div className="patient-info-tabs">
        <Tabs
          defaultActiveKey={
            currentUser !== null && isBACSI(currentUser) ? "info" : ""
          }
          id="patient-info-tabs"
          className="custom-tabs"
          onSelect={(key) => {
            if (key === "history") {
              if (examPatient?.id !== undefined || isBENHNHAN(currentUser)) {
                getHistoryUserRegister();
              }
            } else if (key === "invoices") {
              getPaymentHistoryPatientByName();
            } else console.log("else");
          }}
        >
          {currentUser !== null &&
            isBACSI(currentUser) &&
            examPatient !== null && (
              <Tab eventKey="info" title="Thông tin bệnh nhân">
                <div className="tab-content-area">
                  <div className="patient-details">
                    <table className="table table-bordered">
                      <h4 className="w-100 text-center text text-primary">
                        Thông tin tài khoản
                      </h4>
                      <tbody>
                        <tr>
                          <th>Mã bệnh nhân</th>
                          <td>{examPatient.user.id}</td>
                          <th>Tên tài khoản</th>
                          <td>{examPatient.user.name}</td>
                        </tr>
                        <tr>
                          <th>Giới tính</th>
                          <td>{examPatient.user.gender}</td>
                          <th>Ngày sinh</th>
                          <td>
                            {dayjs(examPatient.user.birthday).format(
                              "DD/MM/YYYY"
                            )}
                          </td>
                        </tr>
                        <tr>
                          <th>Điện thoại</th>
                          <td>{examPatient.user.phone}</td>
                          <th>Địa chỉ</th>
                          <td>{examPatient.user.address}</td>
                        </tr>
                        <tr>
                          <th>Email</th>
                          <td>{examPatient.user.email}</td>
                        </tr>
                      </tbody>
                    </table>

                    <table className="table table-bordered mt-5">
                      <h4 className="w-100 text-center text text-primary">
                        Thông tin người khám
                      </h4>
                      <tbody>
                        <tr>
                          <th>Tên người khám</th>
                          <td>{examPatient.name}</td>
                          <th>Triệu chứng</th>
                          <td>{examPatient.favor}</td>
                        </tr>
                        <tr>
                          <th>Ngày khám</th>
                          <td>
                            {dayjs(examPatient.schedule.date).format(
                              "DD/MM/YYYY"
                            )}
                          </td>
                          <th>Số thứ tự</th>
                          <td>{examPatient.order}</td>
                        </tr>
                      </tbody>
                    </table>
                    <div className="d-flex justify-content-evenly align-item-center">
                      <div>
                        <Link
                          className="btn btn-primary mt-3"
                          to="/examination-form"
                          state={{ examPatient }}
                        >
                          Kê toa
                        </Link>
                      </div>
                      <div>
                        <button
                          className="btn btn-danger mt-3"
                          onClick={() => setExamPatient({})}
                        >
                          Đóng
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </Tab>
            )}
          <Tab eventKey="history" title="Lịch sử khám bệnh">
            <div className="history-container tab-content-area">
              <div className="table-responsive wrapper shadow-lg">
                <table className="table table-scrollable">
                  <thead className="bg-light text-center">
                    <tr className="align-middle">
                      <th>ID</th>
                      <th>Tên người khám</th>
                      <th>Ngày lập phiếu</th>
                      <th>Người khám</th>
                      <th>Triệu chứng</th>
                      <th>Số ngày cấp thuốc</th>
                    </tr>
                  </thead>
                  <tbody className="table-hover text-center">
                    {historyExamsPatient.length > 0 ? (
                      historyExamsPatient.map((h) => {
                        return (
                          <>
                            <tr key={h.id} className="align-middle">
                              <td
                                onClick={() => {
                                  setSelectMedicalExamId(h.id);
                                  getPrescriptionItemsByMedicalExamId(h.id);
                                }}
                                role="button"
                                className="text text-danger underline pointer"
                              >
                                #MSPK{h.id}
                              </td>
                              <td>{h.mrl.name}</td>
                              <td>
                                {dayjs(h.createdDate).format("DD/MM/YYYY")}
                              </td>
                              <td>{h.userCreated.name}</td>
                              <td>{h.symptomProcess}</td>
                              <td>{h.durationDay}</td>
                            </tr>
                            {selectMedicalExamId === h.id && (
                              <>
                                <PrescriptionItems
                                  precriptionItems={precriptionItems}
                                  setSelectMedicalExamId={
                                    setSelectMedicalExamId
                                  }
                                  predict={h.predict}
                                  examPatient={examPatient}
                                  h={h}
                                />
                              </>
                            )}
                          </>
                        );
                      })
                    ) : (
                      <>
                        <tr>
                          <td colSpan="6" className="text-center">
                            <Alert
                              variant="filled"
                              severity="info"
                              className="w-100"
                            >
                              Không có lịch sử khám
                            </Alert>
                          </td>
                        </tr>
                      </>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </Tab>
          {currentUser !== null && isBENHNHAN(currentUser) && (
            <Tab eventKey="invoices" title="Lịch sử hóa đơn">
              <div className="payment-history-container tab-content-area">
                <div className="table-responsive wrapper shadow-lg">
                  <table className="table table-scrollable">
                    <thead className="bg-light text-center">
                      <tr className="align-middle">
                        <th>Mã hóa đơn</th>
                        <th>Ngày thanh toán</th>
                        <th>Tên người khám</th>
                        <th>Số tiền</th>
                        <th>Mô tả hóa đơn</th>
                        <th>Trạng thái</th>
                        <th>Phương thức thanh toán</th>
                      </tr>
                    </thead>
                    <tbody className="table-hover text-center">
                      {historyPaymentPatient.length > 0 ? (
                        historyPaymentPatient.map((hpt) => {
                          return (
                            <>
                              <tr key={hpt.orderId} className="align-middle">
                                <td>{hpt.orderId}</td>
                                <td className="text text-danger">
                                  {dayjs(hpt.createdDate).format("DD/MM/YYYY")}
                                </td>
                                <td>{hpt.name}</td>
                                <td className="text text-danger">
                                  {hpt.amount.toLocaleString("vi-VN")}
                                </td>
                                <td>{hpt.description}</td>
                                <td className="text text-primary">
                                  {hpt.resultCode === "0" || "00"
                                    ? "Thành công"
                                    : ""}
                                </td>
                                <td>{hpt.partnerCode}</td>
                              </tr>
                            </>
                          );
                        })
                      ) : (
                        <>
                          <tr>
                            <td colSpan="12" className="text-center">
                              <Alert
                                variant="filled"
                                severity="info"
                                className="w-100"
                              >
                                Không có lịch sử thanh toán
                              </Alert>
                            </td>
                          </tr>
                        </>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </Tab>
          )}
        </Tabs>
      </div>
    </>
  );
}
