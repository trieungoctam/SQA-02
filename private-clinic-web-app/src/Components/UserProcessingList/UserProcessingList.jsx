import { useCallback, useContext, useEffect, useState } from "react";
import { UserContext } from "../config/Context";
import { authAPI, endpoints } from "../config/Api";
import { CustomerSnackbar, isBACSI } from "../Common/Common";
import { Alert, Pagination } from "@mui/material";
import dayjs from "dayjs";
import "./UserProcessingList.css";
import PatientTabs from "../PatientTabs/PatientTabs";

export default function UserProcessingList() {
  const [userProcessingList, setUserProcessingList] = useState([]);
  const { currentUser } = useContext(UserContext);

  const [examPatient, setExamPatient] = useState({});
  const [historyExamsPatient, setHistoryExamPatient] = useState([]);

  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);

  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Đăng kí thành công",
    severity: "success",
  });

  const showSnackbar = (message, severity) => {
    setData({
      message: message,
      severity: severity,
    });

    setOpen(true);

    setTimeout(() => {
      setOpen(false);
    }, 5000);
  };

  const getAllProcessingUserToday = useCallback(async () => {
    let response;
    if (isBACSI(currentUser) && currentUser !== null) {
      try {
        let url = `${endpoints["getAllProcessingUserToday"]}?page=${page}`;
        response = await authAPI().get(url, {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        });

        if (response.status === 200) {
          setUserProcessingList(response.data);
          setTotalPage(response.data.totalPages);
        } else showSnackbar(response.data, "error");
      } catch {
        showSnackbar("Lỗi1", "error");
      }
    }
  }, [currentUser, page]);

  const getHistoryUserRegister = async () => {
    let response;
    if (isBACSI(currentUser) && currentUser !== null && examPatient !== null) {
      try {
        let url = `${endpoints["getHistoryUserRegister"]}`;
        response = await authAPI().post(
          url,
          {
            email: examPatient.user.email,
            nameRegister: examPatient.name,
          },
          {
            validateStatus: function (status) {
              return status < 500; // Chỉ ném lỗi nếu status code >= 500
            },
          }
        );

        if (response.status === 200) {
          setHistoryExamPatient(response.data);
          console.log(response.data);
        } else showSnackbar(response.data, "error");
      } catch {
        console.log(response);
        showSnackbar("Lỗi2", "error");
      }
    }
  };

  useEffect(() => {
    if (currentUser !== null) {
      getAllProcessingUserToday();
    }
  }, [currentUser, page, examPatient]);

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <div className="container container-user-processing-list">
        <h2 className="text text-primary">Danh sách bệnh nhân đang đợi</h2>
        {userProcessingList.empty === false && (
          <Pagination
            count={totalPage}
            color="primary"
            className="mt-2 mb-4"
            onChange={(event, value) => setPage(value)}
          />
        )}
        <ul className="responsive-table">
          <li className="table-header">
            <div className="col col-1">Mã</div>
            <div className="col col-2">Tên người khám</div>
            <div className="col col-4">Ngày khám</div>
            <div className="col col-5">Số điện thoại</div>
            <div className="col col-6">Địa chỉ</div>
            <div className="col col-7">Triệu chứng</div>
          </li>
          {userProcessingList.empty === true ? (
            <>
              <Alert variant="filled" severity="info" className="w-50 mx-auto">
                Hiện không có phiếu đăng kí nào
              </Alert>
            </>
          ) : (
            <>
              {userProcessingList.empty === false &&
                userProcessingList.content.map((up) => {
                  return (
                    <>
                      <li key={up.id} className="table-row">
                        <div className="col col-1" data-label="ID">
                          #MSPDKLK{up.id}
                        </div>
                        <div
                          role="button"
                          className="col col-2 text text-info underline"
                          data-label="Name Register"
                          onClick={() => setExamPatient(up)}
                        >
                          {up.name}
                        </div>
                        <div className="col col-4" data-label="Date Register">
                          {dayjs(up.schedule.date).format("DD-MM-YYYY")}
                        </div>
                        <div className="col col-5" data-label="Phone">
                          {up.user.phone}
                        </div>
                        <div className="col col-6" data-label="Address">
                          {up.user.address}
                        </div>
                        <div className="col col-7" data-label="Favor">
                          {up.favor}
                        </div>
                      </li>
                      {examPatient.id === up.id && (
                        <PatientTabs
                          examPatient={examPatient}
                          setExamPatient={setExamPatient}
                          historyExamsPatient={historyExamsPatient}
                          setHistoryExamPatient={setHistoryExamPatient}
                          getHistoryUserRegister={getHistoryUserRegister}
                        />
                      )}
                    </>
                  );
                })}
            </>
          )}
        </ul>
      </div>
    </>
  );
}
