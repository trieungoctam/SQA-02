import { Badge, Button, Dropdown } from "react-bootstrap";
import "./NotificationContainer.css";
import { useContext, useEffect, useReducer, useRef, useState } from "react";
import SockJS from "sockjs-client";
import { over } from "stompjs";
import { BASE_URL } from "../config/Api";
import { UserContext } from "../config/Context";
import dayjs from "dayjs";
import { CustomerSnackbar, isBENHNHAN, isTUVAN, isYTA } from "../Common/Common";
import { useNavigate } from "react-router-dom";

export default function NotificationContainer() {
  const [showDropdownYTA, setShowDropdownYTA] = useState(false);
  const [showDropdownBN, setShowDropdownBN] = useState(false);

  const [YTAnotifications, setYTANotifications] = useState([]);
  // const [BENHNHANnotifications, setBENHNHANNotifications] = useState([]);
  const { BENHNHANnotifications, setBENHNHANNotifications } =
    useContext(UserContext);

  const [TUVANnotifications, setTUVANNotifications] = useState([]);
  const [tempNotifications, setTempNotification] = useState([]); // received all output data from Websocket before seperated

  const stompYTAClientRef = useRef(null);
  const stompBENHNHANClientRef = useRef(null);
  const stompTUVANClientRef = useRef(null);

  const { currentUser } = useContext(UserContext);

  const [countIsReadFalse, setCountIsReadFalse] = useState(0);
  const [countIsReadFalseBN, setCountIsReadFalseBN] = useState(0);

  const [, forceUpdate] = useReducer((x) => x + 1, 0);

  const navigate = useNavigate();

  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Thông báo mới",
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
    }, 4000);
  };

  function sliceString(str) {
    const length = str.length;
    const sliceLength = Math.ceil((1 / 2) * length);
    return str.slice(0, sliceLength);
  }

  function formatDuration(seconds) {
    seconds = seconds / 1000;
    if (seconds < 60) {
      seconds = Math.floor(seconds);
      return `${seconds} giây`;
    } else if (seconds < 3600) {
      const minutes = Math.floor(seconds / 60);
      return `${minutes} phút`;
    } else if (seconds < 86400) {
      const hours = Math.floor(seconds / 3600);
      return `${hours} giờ`;
    } else if (seconds < 2592000) {
      const days = Math.floor(seconds / 86400);
      return `${days} ngày`;
    } else if (seconds < 31536000) {
      const months = Math.floor(seconds / 2592000);
      return `${months} tháng`;
    } else {
      const years = Math.floor(seconds / 31536000);
      return `${years} năm`;
    }
  }

  const ytaConnectNotificationWsInit = () => {
    let stompYTAClient = null;
    let socket = new SockJS(`${BASE_URL}/wss`);
    stompYTAClient = over(socket);
    stompYTAClient.debug = () => {}; // tắt log của stomp in ra console
    stompYTAClientRef.current = stompYTAClient;
    stompYTAClient.connect(
      {},
      () => {
        stompYTAClient.subscribe("/notify/registerContainer/", (payload) => {
          const p = JSON.parse(payload.body);
          p.timeSent = Date.now();
          p.isRead = false;
          setYTANotifications((prevYTANotifications) => [
            p,
            ...prevYTANotifications,
          ]);
          showSnackbar("Bạn có thông báo mới", "success");
          forceUpdate(); // bên client đã re-render , do đã navigate và nạp trang list , nhưng bên này để màn hình đứng yên dẫn đến ko đc re render
        });
      },
      onError
    );
    return () => {
      if (stompYTAClientRef.current) {
        stompYTAClientRef.current.disconnect();
        stompYTAClientRef.current = null;
      }
    };
  };

  const benhnhanConnectNotificationWsInit = () => {
    let stompBENHNHANClient = null;
    let socket = new SockJS(`${BASE_URL}/wss`);

    stompBENHNHANClient = over(socket);
    stompBENHNHANClient.debug = () => {}; // tắt log của stomp in ra console
    stompBENHNHANClientRef.current = stompBENHNHANClient;
    stompBENHNHANClient.connect(
      {},
      () => {
        stompBENHNHANClient.subscribe(
          "/notify/directRegister/" + currentUser.id,
          (payload) => {
            const p = JSON.parse(payload.body);
            p.timeSent = Date.now();
            p.isRead = false;
            p.type = "DICRECT_REGISTER";
            setBENHNHANNotifications((prevBNNotifications) => [
              p,
              ...prevBNNotifications,
            ]);
            showSnackbar("Bạn có thông báo mới", "success");
            forceUpdate(); // bên client đã re-render , do đã navigate và nạp trang list , nhưng bên này để màn hình đứng yên dẫn đến ko đc re render
          }
        );
        stompBENHNHANClient.subscribe(
          "/notify/censorSuccessfully/" + currentUser.id,
          (payload) => {
            const p = JSON.parse(payload.body);
            p.timeSent = Date.now();
            p.isRead = false;
            p.type = "CENSOR_SUCCESSFULLY";
            setBENHNHANNotifications((prevBNNotifications) => [
              p,
              ...prevBNNotifications,
            ]);
            showSnackbar("Bạn có thông báo mới", "success");
            forceUpdate(); // bên client đã re-render , do đã navigate và nạp trang list , nhưng bên này để màn hình đứng yên dẫn đến ko đc re render
          }
        );
        stompBENHNHANClient.subscribe(
          "/notify/cashSuccesfully/" + currentUser.id,
          (payload) => {
            const p = JSON.parse(payload.body);
            p.timeSent = Date.now();
            p.isRead = false;
            p.type = "CASH_SUCCESSFULLY";
            setBENHNHANNotifications((prevBNNotifications) => [
              p,
              ...prevBNNotifications,
            ]);
            showSnackbar("Bạn có thông báo mới", "success");
            forceUpdate(); // bên client đã re-render , do đã navigate và nạp trang list , nhưng bên này để màn hình đứng yên dẫn đến ko đc re render
          }
        );
        stompBENHNHANClient.subscribe(
          "/notify/recievedNewComment/" + currentUser.id,
          (payload) => {
            const p = JSON.parse(payload.body);
            p.timeSent = Date.now();
            p.isRead = false;
            p.type = "RECIEVED_NEW_COMMENT";
            setBENHNHANNotifications((prevBNNotifications) => [
              p,
              ...prevBNNotifications,
            ]);
            showSnackbar("Bạn có thông báo mới", "success");
            forceUpdate(); // bên client đã re-render , do đã navigate và nạp trang list , nhưng bên này để màn hình đứng yên dẫn đến ko đc re render
          }
        );
        stompBENHNHANClient.subscribe(
          "/notify/recievedLikeBlog/" + currentUser?.id,
          (payload) => {
            let p = JSON.parse(payload.body);

            p.timeSent = Date.now();
            p.isRead = false;
            p.type = "RECIEVED_LIKED_BLOG";
            p.blogId = p.blog.id;
            setTempNotification((prevTempNotifications) => [
              p,
              ...prevTempNotifications,
            ]);
          }
        );
      },
      onError
    );
    return () => {
      if (stompBENHNHANClientRef.current) {
        stompBENHNHANClientRef.current.disconnect();
        stompBENHNHANClientRef.current = null;
      }
    };
  };

  const tuvanConnectNotificationWsInit = () => {
    let stompTUVANClient = null;
    let socket = new SockJS(`${BASE_URL}/wss`);

    stompTUVANClient = over(socket);
    stompTUVANClient.debug = () => {}; // tắt log của stomp in ra console
    stompTUVANClientRef.current = stompTUVANClient;
    stompTUVANClient.connect({}, () => {});
  };

  useEffect(() => {
    if (
      currentUser !== null &&
      !stompYTAClientRef.current &&
      isYTA(currentUser)
    )
      ytaConnectNotificationWsInit();
    else if (
      currentUser !== null &&
      !stompBENHNHANClientRef.current &&
      isBENHNHAN(currentUser)
    )
      benhnhanConnectNotificationWsInit();
    else if (
      currentUser !== null &&
      !stompTUVANClientRef.current &&
      isTUVAN(currentUser)
    )
      tuvanConnectNotificationWsInit();
    handleCountIsReadFalse(YTAnotifications);
    handleCountIsReadFalseBN(BENHNHANnotifications);
  }, [YTAnotifications, BENHNHANnotifications]);

  useEffect(() => {
    if (currentUser === null) {
      if (stompBENHNHANClientRef.current) {
        stompBENHNHANClientRef.current.disconnect();
        stompBENHNHANClientRef.current = null;
      } else if (stompYTAClientRef.current) {
        stompYTAClientRef.current.disconnect();
        stompYTAClientRef.current = null;
      } else if (stompTUVANClientRef.current) {
        stompTUVANClientRef.current.disconnect();
        stompTUVANClientRef.current = null;
      }
    }
  }, [currentUser]); // this useEffect not sure , can make error

  useEffect(() => {
    if (
      tempNotifications.length > 0 &&
      tempNotifications[0].type === "RECIEVED_LIKED_BLOG"
    ) {
      let updatedItems = [...BENHNHANnotifications];

      let tempNotify = tempNotifications[0];

      let existingLikeBlogNotifyIndex = updatedItems.findIndex((notifyItem) => {
        return (
          notifyItem.type === "RECIEVED_LIKED_BLOG" &&
          notifyItem.blogId === tempNotify.blogId
        );
      });

      let existingLikeBlogNotify = updatedItems[existingLikeBlogNotifyIndex];
      if (existingLikeBlogNotify) {
        existingLikeBlogNotify.isRead = false;
        existingLikeBlogNotify.timeSent = Date.now();
        existingLikeBlogNotify.blog = tempNotify.blog;
        existingLikeBlogNotify.user = tempNotify.user;

        const [element] = updatedItems.splice(existingLikeBlogNotifyIndex, 1);
        updatedItems.unshift(element);

        setBENHNHANNotifications(updatedItems);
      } else {
        setBENHNHANNotifications((prev) => [tempNotify, ...prev]);
      }

      showSnackbar("Bạn có thông báo mới", "success");
      forceUpdate(); // bên client đã re-render , do đã navigate và nạp trang list , nhưng bên này để màn hình đứng yên dẫn đến ko đc re render
    }
  }, [tempNotifications]);

  function onError() {
    console.log("Lỗi");
    console.log("stompYTAClientRef", stompYTAClientRef);
    console.log("stompBENHNHANClientRef", stompBENHNHANClientRef);
  }

  function handleCountIsReadFalse(YTAnotifications) {
    let count = 0;
    if (YTAnotifications.length < 1) {
      setCountIsReadFalse(count);
      return;
    }
    YTAnotifications.map((n) => {
      if (n.isRead === false) ++count;
    });
    setCountIsReadFalse(count);
  }

  function handleCountIsReadFalseBN(BENHNHANnotifications) {
    let count = 0;
    if (BENHNHANnotifications.length < 1) {
      setCountIsReadFalseBN(count);
      return;
    }
    BENHNHANnotifications.map((n) => {
      if (n.isRead === false) ++count;
    });
    setCountIsReadFalseBN(count);
  }

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      {isYTA(currentUser) && (
        <div className="notification-container">
          <Dropdown
            show={showDropdownYTA}
            onToggle={() => setShowDropdownYTA(!showDropdownYTA)}
          >
            <Dropdown.Toggle
              className="d-flex text-center justify-content-between align-items-center bg-success"
              variant="light"
              id="dropdown-basic"
            >
              <i
                className="fa fa-bell text-white mr-3"
                style={{ marginRight: "10px" }}
              ></i>
              <Badge className="bg-danger" variant="danger">
                {countIsReadFalse}
              </Badge>
            </Dropdown.Toggle>

            <Dropdown.Menu
              className="shadow-lg"
              style={{
                width: "300px",
                maxHeight: "400px",
                minHeight: "100px",
                overflowY: "scroll",
                right: "0",
                left: "auto",
              }}
            >
              <Dropdown.Divider />

              {isYTA(currentUser) &&
                YTAnotifications.length > 0 &&
                YTAnotifications.map((notification) => (
                  <Dropdown.Item
                    onClick={() => {
                      notification.isRead = true;
                      navigate("/censor-register");
                      handleCountIsReadFalse(YTAnotifications);
                    }}
                    key={notification.id}
                    className={`d-flex align-items-start border ${
                      notification.isRead ? "" : "bg-warning"
                    }`}
                    style={{
                      fontSize: "12px",
                      color: "#000",
                      backgroundColor: "#fff",
                    }}
                  >
                    <img
                      src={notification.user.avatar}
                      alt="Avatar"
                      className="rounded-circle"
                      style={{
                        width: "40px",
                        height: "40px",
                        marginRight: "10px",
                      }}
                    />
                    <div>
                      <strong>{notification.user.name}</strong>
                      <p
                        className="mb-0"
                        style={{ fontSize: "12px", color: "#fff" }}
                      >
                        <small style={{ fontSize: "12px", color: "#000" }}>
                          Đặt lịch khám vào ngày{" "}
                          {dayjs(notification.schedule.date).format(
                            "DD/MM/YYYY"
                          )}
                        </small>
                        <small
                          style={{
                            display: "block",
                            fontSize: "12px",
                            color: "red",
                          }}
                        >
                          {formatDuration(Date.now() - notification.timeSent)}{" "}
                          trước
                        </small>
                      </p>
                    </div>
                  </Dropdown.Item>
                ))}

              <Dropdown.Divider />
              {YTAnotifications.length > 0 ? (
                <Dropdown.Item
                  className="text-center text-primary"
                  style={{
                    fontSize: "12px",
                    color: "#000",
                    backgroundColor: "#fff",
                  }}
                >
                  Đóng
                </Dropdown.Item>
              ) : (
                <>
                  <p className="text-center">
                    <strong>Hiện tại không có thông báo nào</strong>
                  </p>
                </>
              )}
            </Dropdown.Menu>
          </Dropdown>
        </div>
      )}

      {isBENHNHAN(currentUser) && (
        <div className="notification-container">
          <Dropdown
            show={showDropdownBN}
            onToggle={() => setShowDropdownBN(!showDropdownBN)}
          >
            <Dropdown.Toggle
              className="d-flex text-center justify-content-between align-items-center bg-success"
              variant="light"
              id="dropdown-basic"
            >
              <i
                className="fa fa-bell text-white mr-3"
                style={{ marginRight: "10px" }}
              ></i>
              <Badge className="bg-danger" variant="danger">
                {countIsReadFalseBN}
              </Badge>
            </Dropdown.Toggle>

            <Dropdown.Menu
              className="shadow-lg"
              style={{
                width: "350px",
                maxHeight: "400px",
                minHeight: "100px",
                overflowY: "scroll",
                right: "0",
                left: "auto",
              }}
            >
              <Dropdown.Divider />

              {isBENHNHAN(currentUser) &&
                BENHNHANnotifications.length > 0 &&
                BENHNHANnotifications.map((notification) => {
                  if (notification.type === "DICRECT_REGISTER") {
                    return (
                      <Dropdown.Item
                        onClick={() => {
                          notification.isRead = true;
                          navigate("/user-register-schedule-list");
                          handleCountIsReadFalseBN(BENHNHANnotifications);
                        }}
                        key={notification.id}
                        className={`d-flex align-items-start border ${
                          notification.isRead ? "" : "bg-warning"
                        }`}
                        style={{
                          fontSize: "12px",
                          color: "#000",
                          backgroundColor: "#fff",
                        }}
                      >
                        <img
                          src={notification.user.avatar}
                          alt="Avatar"
                          className="rounded-circle"
                          style={{
                            width: "40px",
                            height: "40px",
                            marginRight: "10px",
                          }}
                        />
                        <div>
                          <strong>Đặt lịch khám trực tiếp thành công !</strong>
                          <p
                            className="mb-0"
                            style={{ fontSize: "12px", color: "#fff" }}
                          >
                            <small style={{ fontSize: "12px", color: "#000" }}>
                              Đặt lịch khám vào ngày{" "}
                              {dayjs(notification.schedule.date).format(
                                "DD/MM/YYYY"
                              )}
                            </small>
                            <small
                              style={{
                                display: "block",
                                fontSize: "12px",
                                color: "red",
                              }}
                            >
                              {formatDuration(
                                Date.now() - notification.timeSent
                              )}{" "}
                              trước
                            </small>
                          </p>
                        </div>
                      </Dropdown.Item>
                    );
                  }
                  if (notification.type === "CENSOR_SUCCESSFULLY") {
                    return (
                      <Dropdown.Item
                        onClick={() => {
                          notification.isRead = true;
                          navigate("/user-register-schedule-list");
                          handleCountIsReadFalseBN(BENHNHANnotifications);
                        }}
                        key={notification.id}
                        className={`d-flex align-items-start border ${
                          notification.isRead ? "" : "bg-warning"
                        }`}
                        style={{
                          fontSize: "12px",
                          color: "#000",
                          backgroundColor: "#fff",
                        }}
                      >
                        <img
                          src={notification.user.avatar}
                          alt="Avatar"
                          className="rounded-circle"
                          style={{
                            width: "40px",
                            height: "40px",
                            marginRight: "10px",
                          }}
                        />
                        <div>
                          <strong>Xét duyệt thành công đơn đăng ký !</strong>
                          <p
                            className="mb-0"
                            style={{ fontSize: "12px", color: "#fff" }}
                          >
                            <small style={{ fontSize: "12px", color: "#000" }}>
                              Đặt lịch khám vào ngày{" "}
                              {dayjs(notification.schedule.date).format(
                                "DD/MM/YYYY"
                              )}
                            </small>
                            <small
                              style={{
                                display: "block",
                                fontSize: "12px",
                                color: "red",
                              }}
                            >
                              {formatDuration(
                                Date.now() - notification.timeSent
                              )}{" "}
                              trước
                            </small>
                          </p>
                        </div>
                      </Dropdown.Item>
                    );
                  }
                  if (notification.type === "CASH_SUCCESSFULLY") {
                    return (
                      <Dropdown.Item
                        onClick={() => {
                          notification.isRead = true;
                          navigate("/user-register-schedule-list");
                          handleCountIsReadFalseBN(BENHNHANnotifications);
                        }}
                        key={notification.id}
                        className={`d-flex align-items-start border ${
                          notification.isRead ? "" : "bg-warning"
                        }`}
                        style={{
                          fontSize: "12px",
                          color: "#000",
                          backgroundColor: "#fff",
                        }}
                      >
                        <img
                          src={notification.user.avatar}
                          alt="Avatar"
                          className="rounded-circle"
                          style={{
                            width: "40px",
                            height: "40px",
                            marginRight: "10px",
                          }}
                        />
                        <div>
                          <strong className="text-wrap">
                            Thanh toán tiền mặt thành công
                            {notification.statusIsApproved.status ===
                            "PROCESSING"
                              ? ` mã phiếu đăng ký : #MSPDKKB${notification.id} !`
                              : ` phiếu khám bệnh của mã phiếu đăng ký : #MSPDKKB${notification.id}`}
                          </strong>
                          <p
                            className="mb-0"
                            style={{ fontSize: "12px", color: "#fff" }}
                          >
                            <small style={{ fontSize: "12px", color: "#000" }}>
                              Đặt lịch khám vào ngày{" "}
                              {dayjs(notification.schedule.date).format(
                                "DD/MM/YYYY"
                              )}
                            </small>
                            <small
                              style={{
                                display: "block",
                                fontSize: "12px",
                                color: "red",
                              }}
                            >
                              {formatDuration(
                                Date.now() - notification.timeSent
                              )}{" "}
                              trước
                            </small>
                          </p>
                        </div>
                      </Dropdown.Item>
                    );
                  }
                  if (notification.type === "RECIEVED_NEW_COMMENT") {
                    return (
                      <>
                        <Dropdown.Item
                          onClick={() => {
                            notification.isRead = true;
                            navigate("/advise-section");
                            handleCountIsReadFalseBN(BENHNHANnotifications);
                          }}
                          key={notification.id}
                          className={`d-flex align-items-start border ${
                            notification.isRead ? "" : "bg-warning"
                          }`}
                          style={{
                            fontSize: "12px",
                            color: "#000",
                            backgroundColor: "#fff",
                          }}
                        >
                          <img
                            src={notification.comment.user.avatar}
                            alt="Avatar"
                            className="rounded-circle"
                            style={{
                              width: "40px",
                              height: "40px",
                              marginRight: "10px",
                            }}
                          />
                          <div>
                            <strong>
                              {notification.comment.user.name} đã trả lời câu
                              hỏi của bạn.
                            </strong>
                            <p
                              className="mb-0"
                              style={{ fontSize: "12px", color: "#fff" }}
                            >
                              <small
                                style={{ fontSize: "12px", color: "#000" }}
                              >
                                Câu hỏi về :{" "}
                                {sliceString(notification.blog.title)}...
                              </small>
                              <small
                                style={{
                                  display: "block",
                                  fontSize: "12px",
                                  color: "red",
                                }}
                              >
                                {formatDuration(
                                  Date.now() - notification.timeSent
                                )}{" "}
                                trước
                              </small>
                            </p>
                          </div>
                        </Dropdown.Item>
                      </>
                    );
                  }
                  if (notification.type === "RECIEVED_LIKED_BLOG") {
                    return (
                      <>
                        <Dropdown.Item
                          onClick={() => {
                            notification.isRead = true;
                            navigate("/advise-section");
                            handleCountIsReadFalseBN(BENHNHANnotifications);
                          }}
                          key={notification.id}
                          className={`d-flex align-items-start border ${
                            notification.isRead ? "" : "bg-warning"
                          }`}
                          style={{
                            fontSize: "12px",
                            color: "#000",
                            backgroundColor: "#fff",
                          }}
                        >
                          <img
                            src={notification.user.avatar}
                            alt="Avatar"
                            className="rounded-circle"
                            style={{
                              width: "40px",
                              height: "40px",
                              marginRight: "10px",
                            }}
                          />
                          <div>
                            <strong className="text-wrap">
                              Gần đây đã có{" "}
                              <strong className="text text-danger d-inline">
                                {notification.blog.totalLikes}
                              </strong>{" "}
                              người thích câu hỏi của bạn
                            </strong>
                            <p
                              className="mb-0"
                              style={{ fontSize: "12px", color: "#fff" }}
                            >
                              <small
                                style={{ fontSize: "12px", color: "#000" }}
                              >
                                Câu hỏi về :{" "}
                                {sliceString(notification.blog.title)}...
                              </small>
                              <small
                                style={{
                                  display: "block",
                                  fontSize: "12px",
                                  color: "red",
                                }}
                              >
                                {formatDuration(
                                  Date.now() - notification.timeSent
                                )}{" "}
                                trước
                              </small>
                            </p>
                          </div>
                        </Dropdown.Item>
                      </>
                    );
                  }
                })}

              <Dropdown.Divider />
              {BENHNHANnotifications.length > 0 ? (
                <Dropdown.Item
                  className="text-center text-primary"
                  style={{
                    fontSize: "12px",
                    color: "#000",
                    backgroundColor: "#fff",
                  }}
                >
                  Đóng
                </Dropdown.Item>
              ) : (
                <>
                  <p className="text-center">
                    <strong>Hiện tại không có thông báo nào</strong>
                  </p>
                </>
              )}
            </Dropdown.Menu>
          </Dropdown>
        </div>
      )}
    </>
  );
}
