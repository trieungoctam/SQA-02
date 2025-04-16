import { useContext, useEffect, useRef, useState } from "react";
import "./ManageExerciseTime.css";
import { UserContext } from "../config/Context";
import { authAPI, endpoints } from "../config/Api";
import { CustomerSnackbar } from "../Common/Common";
import { format } from "date-fns";
import WalletHistory from "../WalletHistory/WalletHistory";

export default function ManageExerciseTime() {
  const { currentUser } = useContext(UserContext);
  const [isClockIn, setIsClockIn] = useState(false);
  const [attendanceToday, setAttendanceToday] = useState(null);
  const [totalPeriod, setTotalPeriod] = useState(0);
  const [walletBalance, setWalletBalance] = useState(0);

  const [formWithDrawAmount, setformWithDrawAmount] = useState({
    withDraw: 1000,
    note: "",
  });

  const walletHistoryRef = useRef();

  function handleFormWithDrawAmount(e) {
    const { name, value } = e.target;
    setformWithDrawAmount((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  const [currentTime, setCurrentTime] = useState(new Date());
  const startTime = new Date();
  const endTime = new Date();

  // Giờ bắt đầu và kết thúc
  startTime.setHours(5, 30, 0);
  endTime.setHours(6, 0, 0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    handleGetAttendanceExerciseToday();
  }, [isClockIn]);

  useEffect(() => {}, [isClockIn, attendanceToday]);

  useEffect(() => {
    if (isClockIn === false) {
      handleGetTotalPeriodAttendanceExercise();
    }
  }, [isClockIn]);

  useEffect(() => {
    if (isClockIn === false) {
      handleGetCurrentUserWalletBalance();
    }
  }, [isClockIn]);

  useEffect(() => {
    handleGetCurrentUserWalletBalance();
  }, [formWithDrawAmount]);

  const isButtonEnabled =
    currentTime >= new Date(startTime.getTime() - 60 * 60 * 1000) &&
    currentTime <= new Date(endTime.getTime() + 120 * 60 * 1000);

  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Chấm công thành công",
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
    }, 2400);
  };

  const handleGetAttendanceExerciseToday = async () => {
    let response;

    try {
      response = await authAPI().get(endpoints["getAttendanceExerciseToday"], {
        validateStatus: function (status) {
          return status < 500;
        },
      });
      if (response.status === 200) {
        setIsClockIn(true);
        setAttendanceToday(response.data);
      } else if (response.status === 204) {
        setIsClockIn(false);
      } else {
        showSnackbar(response, "error");
      }
    } catch {
      showSnackbar(response, "error");
    }
  };

  const handleClockAttendanceExercise = async (event) => {
    event.preventDefault();

    let response;
    if (isClockIn === false && attendanceToday === null) {
      try {
        response = await authAPI().get(endpoints["clockInAttendanceExercise"], {
          validateStatus: function (status) {
            return status < 500;
          },
        });
        if (response.status === 200) {
          showSnackbar("Chấm công vào giờ thành công !", "success");
          setIsClockIn(true);
        } else {
          showSnackbar(response, "error");
        }
      } catch {
        showSnackbar(response, "error");
      }
    } else {
      try {
        response = await authAPI().get(
          endpoints["clockOutAttendanceExercise"],
          {
            validateStatus: function (status) {
              return status < 500;
            },
          }
        );
        if (response.status === 200) {
          showSnackbar("Chấm công ra về thành công !", "success");
          setIsClockIn(false);
        } else {
          showSnackbar(response, "error");
        }
      } catch {
        showSnackbar(response, "error");
      }
    }
  };

  const handleGetTotalPeriodAttendanceExercise = async () => {
    let response;
    try {
      response = await authAPI().get(
        endpoints["totalPeriodAttendanceExercise"],
        {
          validateStatus: function (status) {
            return status < 500;
          },
        }
      );
      if (response.status === 200) {
        setTotalPeriod(response.data);
      } else {
        showSnackbar(response, "error");
      }
    } catch {
      showSnackbar(response, "error");
    }
  };

  const handleGetCurrentUserWalletBalance = async () => {
    let response;
    try {
      response = await authAPI().get(endpoints["getCurrentUserWalletBalance"], {
        validateStatus: function (status) {
          return status < 500;
        },
      });
      if (response.status === 200) {
        setWalletBalance(response.data);
      } else {
        showSnackbar(response, "error");
      }
    } catch {
      showSnackbar(response, "error");
    }
  };

  const handleWithDrawAmountFromWallet = async (event) => {
    event.preventDefault();

    let response;
    try {
      response = await authAPI().post(
        endpoints["withDrawAmountFromWallet"],
        {
          ...formWithDrawAmount,
        },
        {
          validateStatus: function (status) {
            return status < 500;
          },
        }
      );
      if (response.status === 200) {
        setformWithDrawAmount({
          withDraw: 1000,
          note: "",
        });
        showSnackbar("Rút tiền thành công !", "success");
      } else {
        showSnackbar(response, "error");
      }
    } catch {
      showSnackbar(response, "error");
    }
  };

  function handleOpenWalletHistoryForm() {
    walletHistoryRef.current.open();
  }

  function handleCloseWalletHistoryForm() {
    walletHistoryRef.current.close();
  }

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <WalletHistory ref={walletHistoryRef} onCancel={handleCloseWalletHistoryForm} />
      <div className="manage-exercise-container">
        <div className="container py-3">
          <div className="row justify-content-center">
            <div className="col-md-10">
              <div className="profile-card text-center">
                <div className="d-flex justify-content-around">
                  <div className="h-100 w-50">
                    <img
                      src={currentUser?.avatar}
                      alt="Profile Picture"
                      className="rounded-circle mb-3 img-avatar"
                    />
                    <h5>Hi, {currentUser?.name}</h5>
                    <p className="text-success">HEALTH CARE</p>
                  </div>
                  <div className="d-flex flex-column justify-content-between align-items-around w-100">
                    <div className="wallet-balance">
                      <h4>
                        <strong className="text text-primary">
                          Số tiền quy đổi :{" "}
                        </strong>
                        <span>
                          <span className="text text-success">
                            {" "}
                            {walletBalance.toLocaleString("vi-VN")}{" "}
                          </span>{" "}
                          đồng
                        </span>
                      </h4>
                    </div>
                    <form onSubmit={handleWithDrawAmountFromWallet}>
                      <div className="d-flex justify-content-around align-items-end">
                        <div>
                          <h5>Số tiền rút : </h5>
                          <span>
                            <input
                              className="w-100"
                              type="number"
                              value={formWithDrawAmount.withDraw}
                              onChange={handleFormWithDrawAmount}
                              name="withDraw"
                              min={1000}
                              max={walletBalance}
                              required
                            />
                          </span>
                        </div>
                        <div>
                          <h5>Ghi chú : </h5>
                          <span>
                            <input
                              type="text"
                              value={formWithDrawAmount.note}
                              onChange={handleFormWithDrawAmount}
                              name="note"
                              required
                            />
                          </span>
                        </div>
                        <div>
                          <button type="submit" className="btn btn-danger">
                            Rút tiền
                          </button>
                        </div>
                        <div>
                          <button
                            onClick={handleOpenWalletHistoryForm}
                            type="button"
                            className="btn btn-primary"
                          >
                            Xem LS giao dịch
                          </button>
                        </div>
                      </div>
                    </form>
                    <div className="total-attendance-time">
                      <h4>
                        <strong className="text text-primary">
                          Số phút tập luyện :{" "}
                        </strong>
                        <span>
                          <span className="text text-danger">
                            {" "}
                            {totalPeriod}{" "}
                          </span>{" "}
                          phút
                        </span>
                      </h4>
                    </div>
                  </div>
                </div>

                <div className="time-box">
                  <p className="d-flex align-items-start">
                    Giờ bắt đầu :
                    <strong id="start-time" className="ml-3">
                      {format(startTime, "HH:mm:ss")}
                    </strong>
                  </p>
                </div>

                {attendanceToday !== null && attendanceToday?.clockIn && (
                  <div className="time-box2">
                    <p className="d-flex align-items-start">
                      Ghi nhận vào lúc :
                      <strong id="start-time" className="ml-3">
                        {format(
                          new Date(attendanceToday?.clockIn),
                          "dd-MM-yyyy HH:mm:ss"
                        )}
                      </strong>
                    </p>
                  </div>
                )}
                {isClockIn === false && attendanceToday === null && (
                  <div>
                    <h1 className="my-4" id="current-time">
                      <strong>{currentTime.toLocaleTimeString("en-GB")}</strong>
                    </h1>
                    <button
                      onClick={(e) => handleClockAttendanceExercise(e)}
                      id="enter-button"
                      className={`btn btn-custom ${
                        isButtonEnabled ? "" : "bg-secondary"
                      }`}
                      disabled={!isButtonEnabled}
                    >
                      {isClockIn === true ? "Về" : "Vào"}
                    </button>
                  </div>
                )}
                <div className="time-box mt-3">
                  <p className="d-flex align-items-start">
                    Giờ kết thúc :{" "}
                    <strong id="end-time">{format(endTime, "HH:mm:ss")}</strong>
                  </p>
                </div>

                {isClockIn === true &&
                  attendanceToday?.clockIn &&
                  attendanceToday?.clockOut === null && (
                    <div>
                      <h1 className="my-4" id="current-time">
                        <strong>
                          {currentTime.toLocaleTimeString("en-GB")}
                        </strong>
                      </h1>
                      <button
                        onClick={(e) => handleClockAttendanceExercise(e)}
                        id="enter-button"
                        className={`btn btn-custom ${
                          isButtonEnabled ? "" : "bg-secondary"
                        }`}
                        disabled={!isButtonEnabled}
                      >
                        {isClockIn === true ? "Về" : "Vào"}
                      </button>
                    </div>
                  )}

                {attendanceToday !== null && attendanceToday?.clockOut && (
                  <div className="time-box2">
                    <p className="d-flex align-items-start">
                      Ghi nhận vào lúc :
                      <strong id="start-time" className="ml-3">
                        {format(
                          new Date(attendanceToday?.clockOut),
                          "dd-MM-yyyy HH:mm:ss"
                        )}
                      </strong>
                    </p>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
