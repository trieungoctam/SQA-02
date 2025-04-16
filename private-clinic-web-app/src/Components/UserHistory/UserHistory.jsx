import { useCallback, useContext, useEffect, useState } from "react";
import "./UserHistory.css";
import { CustomerSnackbar, isBENHNHAN } from "../Common/Common";
import { authAPI, endpoints } from "../config/Api";
import { UserContext } from "../config/Context";
import dayjs from "dayjs";
import { Alert, Pagination } from "@mui/material";
import PatientTabs from "../PatientTabs/PatientTabs";

export default function UserHistory() {
  const [allUserHistoryList, setAllUserHistoryList] = useState([]);
  const { currentUser } = useContext(UserContext);
  const [namePatient, setNamePatient] = useState(null);
  const [historyExamsPatient, setHistoryExamPatient] = useState([]);
  const [historyPaymentPatient, setHistoryPaymentPatient] = useState([]);

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

  useEffect(() => {
    if (currentUser !== null && isBENHNHAN(currentUser))
      getMrlAndMeUserHistory();
  }, [currentUser,page]);

  const getMrlAndMeUserHistory = useCallback(async () => {
    let response;
    if (isBENHNHAN(currentUser) && currentUser !== null) {
      try {
        let url = `${endpoints["getMrlAndMeUserHistory"]}?page=${page}`;
        response = await authAPI().get(url, {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        });

        if (response.status === 200) {
          setAllUserHistoryList(response.data);
          setTotalPage(response.data.totalPages);
        } else showSnackbar(response.data, "error");
      } catch {
        showSnackbar("Lỗi1", "error");
      }
    }
  }, [currentUser, page]);

  const getHistoryUserRegister = async () => {
    let response;
    if (
      isBENHNHAN(currentUser) &&
      currentUser !== null &&
      namePatient !== null
    ) {
      try {
        let url = `${endpoints["getHistoryUserRegister"]}`;
        response = await authAPI().post(
          url,
          {
            email: currentUser?.email,
            nameRegister: namePatient,
          },
          {
            validateStatus: function (status) {
              return status < 500; // Chỉ ném lỗi nếu status code >= 500
            },
          }
        );

        if (response.status === 200) {
          setHistoryExamPatient(response.data);
        } else showSnackbar(response.data, "error");
      } catch {
        console.log(response);
        showSnackbar("Lỗi2", "error");
      }
    }
  };

  const getPaymentHistoryPatientByName = async () => {
    let response;
    if (
      isBENHNHAN(currentUser) &&
      currentUser !== null &&
      namePatient !== null
    ) {
      try {
        let url = `${endpoints["getPaymentHisotoryByName"]}`;
        response = await authAPI().post(
          url,
          {
            name: namePatient,
          },
          {
            validateStatus: function (status) {
              return status < 500;
            },
          }
        );

        if (response.status === 200) {
          setHistoryPaymentPatient(response.data);
        } else showSnackbar(response.data, "error");
      } catch {
        console.log(response);
        showSnackbar(response, "error");
      }
    }
  };

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <div className="container container-user-processing-list">
        <h2 className="text text-primary">Lịch sử khám bệnh</h2>
        {allUserHistoryList.empty === false && (
          <Pagination
            count={totalPage}
            color="primary"
            className="mt-2 mb-4"
            onChange={(event, value) => setPage(value)}
          />
        )}
        <ul className="responsive-table">
          <li className="table-header">
            <div className="col col-2">Tên người khám</div>
            <div className="col col-4">Ngày khám gần nhất</div>
            <div className="col col-5">Số lần khám</div>
          </li>
          {allUserHistoryList.empty === true ? (
            <>
              <Alert variant="filled" severity="info" className="w-50 mx-auto">
                Hiện không có phiếu đăng kí nào
              </Alert>
            </>
          ) : (
            <>
              {allUserHistoryList.empty === false &&
                allUserHistoryList.content.map((uh) => {
                  return (
                    <>
                      <li key={uh.name} className="table-row">
                        <div
                          role="button"
                          className="col col-2 text text-info underline"
                          data-label="Name Register"
                          onClick={() => setNamePatient(uh.name)}
                        >
                          {uh.name}
                        </div>
                        <div className="col col-4" data-label="Date Register">
                          {dayjs(uh.lastestDate).format("DD-MM-YYYY")}
                        </div>
                        <div className="col col-5" data-label="total">
                          {uh.total}
                        </div>
                      </li>
                      {namePatient === uh.name && (
                        <PatientTabs
                          historyExamsPatient={historyExamsPatient}
                          setHistoryExamPatient={setHistoryExamPatient}
                          getHistoryUserRegister={getHistoryUserRegister}
                          historyPaymentPatient={historyPaymentPatient}
                          setHistoryPaymentPatient={setHistoryPaymentPatient}
                          getPaymentHistoryPatientByName={getPaymentHistoryPatientByName}
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
