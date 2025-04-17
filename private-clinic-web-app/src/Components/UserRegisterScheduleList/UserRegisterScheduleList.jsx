import { useCallback, useContext, useEffect, useRef, useState } from "react";
import "./UserRegisterScheduleList.css";
import { authAPI, endpoints } from "../config/Api";
import { CustomerSnackbar, isBENHNHAN } from "../Common/Common";
import { Alert, Pagination } from "@mui/material";
import dayjs from "dayjs";
import DeleteConfirmationForm from "../DeleteConfirmationForm/DeleteConfirmationForm";
import { UserContext } from "../config/Context";
import PaymentForm from "../PaymentForm/PaymentForm";
import LineProcessing from "../LineProcessing/LineProcessing";

export default function UserRegisterScheduleList() {
  const [userRegisterScheduleList, setUserRegisterScheduleList] = useState([]);
  const [registerScheduleId, setRegisterScheduleId] = useState(null);
  const [urs, setUrs] = useState(null);
  const [me, setMe] = useState(null);
  const [pis, setPis] = useState(null);

  const { BENHNHANnotifications } = useContext(UserContext);

  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);
  const { currentUser } = useContext(UserContext);

  const [isCanceled, setIsCanceled] = useState(false);

  const [voucherGift, setVoucherGift] = useState(null);

  const lineProcessRef = useRef();

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

  const deleteFormRef = useRef();
  const paymentFormRef = useRef();

  const loadUserRegisterScheduleList = useCallback(async () => {
    let response;
    if (isBENHNHAN(currentUser) && currentUser != null) {
      try {
        let url = `${endpoints["userRegisterScheduleList"]}?page=${page}`;
        response = await authAPI().get(url, {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        });

        if (response.status === 200) {
          setUserRegisterScheduleList(response.data);
          setTotalPage(response.data.totalPages);
          // setMe(null);
          // setPis(null);
        } else {
          showSnackbar(response.data, "error");
          setMe(null);
          setPis(null);
        }
      } catch {
        showSnackbar("Lỗi", "error");
      }
    }
  }, [page, currentUser, isCanceled, BENHNHANnotifications, urs]);

  useEffect(() => {
    if (currentUser !== null) {
      loadUserRegisterScheduleList();
      setIsCanceled(false);
      setMe(null);
      setPis(null);
    }
  }, [page, currentUser, isCanceled, BENHNHANnotifications]);

  useEffect(() => {
    if (currentUser !== null) {
      loadUserRegisterScheduleList();
      setIsCanceled(false);
    }
  }, [urs]);

  const handleCancelRegisterSchedule = async (registerScheduleId) => {
    try {
      const response = await authAPI().patch(
        endpoints["userCancelRegisterSchedule"](registerScheduleId),
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );
      if (response.status === 200) {
        showSnackbar("Hủy lịch thành công !", "success");
        handleCloseDeleteConfirmForm();
        setIsCanceled(true);
      } else {
        showSnackbar(response.data, "error");
        handleCloseDeleteConfirmForm();
      }
    } catch {
      showSnackbar("Không thể hủy lịch khám", "error");
    }
  };

  const getMEByMrlId = async (mrlId) => {
    let response;
    try {
      response = await authAPI().get(endpoints["benhnhanGetMEByMrlId"](mrlId), {
        validateStatus: function (status) {
          return status < 500;
        },
      });
      if (response.status === 200) {
        setMe(response.data.me);
        setPis(response.data.pis);
      } else showSnackbar(response.data, "error");
    } catch {
      showSnackbar("Lỗi", "error");
    }
  };

  function handleOpenDeleteConfirmForm(registerScheduleId) {
    deleteFormRef.current.open();
    setRegisterScheduleId(registerScheduleId);
  }

  function handleCloseDeleteConfirmForm() {
    setIsCanceled(false);
    deleteFormRef.current.close();
  }

  function handleOpenPaymentPhase1Form(urs) {
    setMe(null);
    setPis(null);
    setUrs(() => urs);
    paymentFormRef.current.open();
  }

  function handleOpenPaymentPhase2Form(urs) {
    setUrs(() => urs);
    getMEByMrlId(urs.id);
    paymentFormRef.current.open();
  }

  function handleClosePaymentForm() {
    paymentFormRef.current.close();
    setMe(null);
    setPis(null);
  }

  function handleOpenLineProcessForm(urs) {
    lineProcessRef.current.open();
    setUrs(urs);
  }

  function handleCloseLineProcessForm() {
    lineProcessRef.current.close();
    setUrs(null);
  }

  return (
    <>
      <DeleteConfirmationForm
        ref={deleteFormRef}
        onDelete={() => handleCancelRegisterSchedule(registerScheduleId)}
        onCancel={handleCloseDeleteConfirmForm}
      />
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <PaymentForm
        ref={paymentFormRef}
        onCancel={handleClosePaymentForm}
        urs={urs}
        me={me}
        pis={pis}
      />
      <LineProcessing
        ref={lineProcessRef}
        onClose={handleCloseLineProcessForm}
        urs={urs}
        setUrs={setUrs}
        voucherGift={voucherGift}
        setVoucherGift={setVoucherGift}
      />
      {userRegisterScheduleList.empty !== true && (
        <Pagination
          count={totalPage}
          color="primary"
          className="mt-4"
          onChange={(event, value) => setPage(value)}
        />
      )}
      <div className="container container-user-register-schedule-list">
        <h2 className="text text-primary fs-1">
          Danh sách lịch sử đặt lịch khám
        </h2>
        <ul className="responsive-table">
          <li className="table-header">
            <div className="col col-1">Mã phiếu khám</div>
            <div className="col col-2">Ngày đặt</div>
            <div className="col col-3">Tên người khám</div>
            <div className="col col-4">Ngày hẹn khám</div>
            <div className="col col-5">Trạng thái</div>
            <div className="col col-6">Ghi chú</div>
            <div className="col col-7">Hành động</div>
          </li>
          {userRegisterScheduleList.empty === true ? (
            <>
              <Alert variant="filled" severity="info" className="w-50 mx-auto">
                Hiện không có phiếu đăng kí nào
              </Alert>
            </>
          ) : (
            <>
              {userRegisterScheduleList.empty === false &&
                userRegisterScheduleList.content.map((urs) => {
                  return (
                    <li key={urs.id} className="table-row">
                      <div
                        role="button"
                        className="col col-1 text text-info underline"
                        data-label="id"
                        onClick={() => handleOpenLineProcessForm(urs)}
                      >
                        #MSPDKKB{urs.id}
                      </div>
                      <div className="col col-2" data-label="Date Created">
                        {dayjs(urs.createdDate).format("DD-MM-YYYY HH:mm:ss")}
                      </div>
                      <div className="col col-3" data-label="Name Register">
                        {urs.name}
                      </div>
                      <div className="col col-4" data-label="Date Register">
                        {dayjs(urs.schedule.date).format("DD-MM-YYYY HH:mm:ss")}
                      </div>
                      <div className="col col-5" data-label="Status Register">
                        {urs.statusIsApproved.status}
                      </div>
                      <div className="col col-6" data-label="Note">
                        {urs.statusIsApproved.note}
                      </div>
                      {urs.statusIsApproved.status !== "PAYMENTPHASE1" &&
                      urs.statusIsApproved.status !== "PAYMENTPHASE2" ? (
                        <button
                          onClick={() => handleOpenDeleteConfirmForm(urs.id)}
                          className={`col col-7 btn ${
                            urs.statusIsApproved.status !== "CHECKING"
                              ? "btn-secondary disabled"
                              : "btn-danger"
                          }`}
                          data-label="Canceled Register"
                        >
                          Hủy lịch khám
                        </button>
                      ) : (
                        <>
                          {urs.statusIsApproved.status === "PAYMENTPHASE1" && (
                            <button
                              className="col col-7 btn btn-success"
                              onClick={() => {
                                handleOpenPaymentPhase1Form(urs);
                              }}
                            >
                              Thanh toán lấy mã QR
                            </button>
                          )}
                          {urs.statusIsApproved.status === "PAYMENTPHASE2" && (
                            <button
                              className="col col-7 btn btn-success"
                              onClick={() => {
                                handleOpenPaymentPhase2Form(urs);
                              }}
                            >
                              Thanh toán lấy thuốc
                            </button>
                          )}
                        </>
                      )}
                    </li>
                  );
                })}
            </>
          )}
        </ul>
      </div>
    </>
  );
}
