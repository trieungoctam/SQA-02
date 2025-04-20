import { Tab, Tabs } from "react-bootstrap";
import "./PrescriptionItems.css";
import { useContext, useState } from "react";
import { Alert } from "@mui/material";
import { Link } from "react-router-dom";
import { isBACSI } from "../Common/Common";
import { UserContext } from "../config/Context";

export default function PrescriptionItems({
  precriptionItems,
  setSelectMedicalExamId,
  predict,
  examPatient,
  h,
}) {
  const { currentUser } = useContext(UserContext);

  return (
    <>
      <div className="medical-info-tabs">
        <Tabs
          defaultActiveKey="prescriptionItems"
          id="medical-info-tabs"
          className="custom-tabs"
        >
          <Tab eventKey="prescriptionItems" title="Thông tin đơn thuốc">
            <div className="prescriptionItems-container tab-content-area h-25">
              <div className="table-responsive wrapper shadow-lg">
                <table className="table table-scrollable">
                  <thead className="text-center">
                    <tr className="align-middle">
                      <th>Mã thuốc</th>
                      <th>Tên thuốc</th>
                      <th>Đơn vị thuốc</th>
                      <th>Số lượng</th>
                      <th>Cách dùng</th>
                    </tr>
                  </thead>
                  <tbody className="table-hover text-center">
                    {precriptionItems.length < 1 ? (
                      <>
                        <td></td>
                        <td></td>
                        <td>
                          <Alert
                            variant="filled"
                            severity="info"
                            className="w-100 mx-auto bg-info"
                          >
                            Đơn thuốc này không được cấp thuốc
                          </Alert>
                        </td>
                        <td></td>
                        <td></td>
                      </>
                    ) : (
                      precriptionItems.map((p) => {
                        return (
                          <>
                            <tr key={p.id} className="align-middle">
                              <td>{p.medicine.id}</td>
                              <td>{p.medicine.name}</td>
                              <td>{p.medicine.unitType.unitName}</td>
                              <td>{p.prognosis}</td>
                              <td>{p.usage}</td>
                            </tr>
                          </>
                        );
                      })
                    )}
                  </tbody>
                </table>
                <h4 className="text-primary text-start">
                  Chẩn đoán : <strong className="text-danger">{predict}</strong>
                </h4>

                <div className="d-flex justify-content-evenly align-item-center">
                  {isBACSI(currentUser) && (
                    <Link
                      className="btn btn-primary mt-3"
                      to="/examination-form"
                      state={{ examPatient, precriptionItems, h }}
                    >
                      Nạp đơn lên phiếu khám
                    </Link>
                  )}
                  <button
                    onClick={() => setSelectMedicalExamId(0)}
                    className="btn btn-danger mt-3"
                  >
                    Đóng
                  </button>
                </div>
              </div>
            </div>
          </Tab>
        </Tabs>
      </div>
    </>
  );
}
