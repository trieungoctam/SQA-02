import { useCallback, useContext, useReducer, useRef, useState } from "react";
import "./Chatting.css";
import { useEffect } from "react";
import { UserContext } from "../config/Context";
import { authAPI, BASE_URL, endpoints } from "../config/Api";
import { CustomerSnackbar, isBENHNHAN } from "../Common/Common";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import OnlineSide from "../OnlineSide/OnlineSide";
import Select from "react-select";

export default function Chatting() {
  const [userList, setUserList] = useState([]);
  let userSelectRef = useRef(undefined);

  const { currentUser } = useContext(UserContext);
  const [chatRooms, setChatRooms] = useState(null);
  const [recipient, setRecipient] = useState(null);

  const [tempOutputWsInputSender, setTempOutputWsInputSender] = useState(null);

  let stompUSERClientRef = useRef(null);

  let [messageContent, setMessageContent] = useState("");
  const [messagesContainer, setMessagesContainer] = useState([]);

  const [onlineUsers, setOnlineUsers] = useState([]);

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
    if (currentUser !== null && !stompUSERClientRef.current)
      userConnectChattingWsInit();
    if (userList.length < 1) getAllUsers();
  }, []);

  useEffect(() => {
    const element = document.getElementById("chatting-container");
    element.scrollIntoView();

    getAllRecipientBySender();
  }, [messagesContainer, onlineUsers, tempOutputWsInputSender]);

  useEffect(() => {
    if (document !== null) {
      let element = document.getElementById("chatting-content-main");
      if (element !== null) {
        document.getElementById("chatting-content-main").scrollTop =
          document.getElementById("chatting-content-main").scrollHeight;
      }
    }
  }, [messagesContainer, messageContent]);

  useEffect(() => {
    if (document !== null) {
      document.body.style.overflow = "hidden";
      return () => {
        document.body.style.overflow = "auto";
      };
    }
  }, [document]);

  useEffect(() => {
    if (tempOutputWsInputSender !== null && recipient !== null) {
      if (tempOutputWsInputSender.sender.id === recipient.id) {
        setMessagesContainer((prev) => [...prev, tempOutputWsInputSender]);
      }
    }
  }, [tempOutputWsInputSender]);

  const getAllRecipientBySender = async () => {
    let response;

    try {
      response = await authAPI().get(endpoints["getAllRecipientBySender"], {
        validateStatus: function (status) {
          return status < 500;
        },
      });
      if (response.status === 200) {
        setChatRooms(response.data);
      }
    } catch {}
  }; // removed chatRoom

  const getAllChatMessageBySenderAndRecipient = async (recipient) => {
    let response;
    try {
      response = await authAPI().post(
        endpoints["getAllChatMessageBySenderAndRecipient"],
        {
          senderId: currentUser?.id,
          recipientId: recipient?.id,
        },
        {
          validateStatus: function (status) {
            return status < 500;
          },
        }
      );
      if (response.status === 200) {
        setMessagesContainer([...response.data]);
      } else showSnackbar(response?.data, "error");
    } catch {
      showSnackbar(response?.data, "error");
    }
  };

  function hanldeClickRecipientItem(recipient) {
    setRecipient(recipient);
    getAllChatMessageBySenderAndRecipient(recipient);
  }

  const connentToConsultant = async () => {
    let response;
    try {
      response = await authAPI().get(endpoints["connentToConsultant"], {
        validateStatus: function (status) {
          return status < 500; // Chỉ ném lỗi nếu status code >= 500
        },
      });
      if (response.status === 200) {
        showSnackbar("Kết nối thành công !", "success");
        setRecipient(response.data);
      } else if (response.status === 204) {
        showSnackbar(
          "Hiện không có tư vấn viên nào đang trực ! Vui lòng kết nối lại sau !",
          "error"
        );
      }
    } catch {
      showSnackbar(response, "error");
    }
  };

  function onMessageReceived(payload) {
    let p = JSON.parse(payload.body);

    setTempOutputWsInputSender(p);
  }

  const userConnectChattingWsInit = () => {
    let stompUSERClient = null;
    let socket = new SockJS(`${BASE_URL}/ws`);
    stompUSERClient = over(socket);
    stompUSERClient.debug = () => {}; // tắt log của stomp in ra console
    stompUSERClientRef.current = stompUSERClient;
    stompUSERClient.connect(
      { "Access-Control-Allow-Origin": `*` },
      () => {
        stompUSERClient.subscribe(
          `/user/${currentUser?.id}/queue/messages`,
          onMessageReceived
        );
        stompUSERClient.subscribe(`/user/public`, onMessageReceived);

        stompUSERClient.send(
          "/app/online.addOnlineUser",
          {},
          JSON.stringify({
            userId: currentUser?.id,
          })
        );

        stompUSERClient.subscribe("/online-users", (payload) => {
          let p = JSON.parse(payload.body);
          setOnlineUsers((prev) => [...prev, p]);
        });
      },
      onError
    );
    return () => {
      if (stompUSERClientRef.current) {
        stompUSERClientRef.current.disconnect();
        stompUSERClientRef.current = null;
      }
    };
  };

  function onError() {
    console.log("Lỗi");
    console.log("stompUSERClientRef", stompUSERClientRef);
  }

  function handleSubmitSendMessage(event) {
    event.preventDefault();

    messageContent = messageContent.trim();

    if (messageContent && stompUSERClientRef.current !== null) {
      const chatMessage = {
        senderId: currentUser?.id,
        recipientId: recipient?.id,
        content: messageContent,
        createdDate: new Date(),
      };
      stompUSERClientRef.current.send(
        "/app/chat",
        {},
        JSON.stringify(chatMessage)
      );

      setMessagesContainer((prev) => [
        ...prev,
        {
          sender: currentUser,
          recipient: recipient,
          content: messageContent,
          createdDate: new Date(),
        },
      ]);
      setMessageContent("");
    }
    document.getElementById("messageSendBox").value = "";
  }

  const renderMessages = (messagesContainer, currentUser) => {
    if (!messagesContainer?.length) {
      return null;
    }

    return messagesContainer.map((m) => {
      if (currentUser?.id === m?.sender?.id) {
        return (
          <div
            className="d-flex mb-3 mr-3 flex-row-reverse align-items-center"
            key={m.id}
          >
            <div className="message bg-primary text-white">
              <p className="mb-0 text-break">{m.content}</p>
            </div>
          </div>
        );
      } else {
        return (
          <div className="d-flex mb-3 align-items-start" key={m.id}>
            <img
              src={m?.sender.avatar}
              className="rounded-circle me-2"
              alt="User Avatar"
            />
            <div className="message bg-light">
              <p className="mb-0 text-break">{m.content}</p>
            </div>
          </div>
        );
      }
    });
  };

  function toggleLockChatScreen() {
    const lockIcon = document.getElementById("lock-icon");

    if (lockIcon.classList.contains("fa-lock-open")) {
      lockIcon.classList.remove("fa-lock-open");
      lockIcon.style.color = "#f50505";
      document.body.style.overflow = "hidden";
    } else {
      lockIcon.classList.add("fa-lock-open");
      lockIcon.style.color = "#63E6BE";
      document.body.style.overflow = "auto";
    }
  }

  const getAllUsers = useCallback(async () => {
    let response;
    try {
      response = await authAPI().get(endpoints["getAllUsers"], {
        validateStatus: function (status) {
          return status < 500; // Chỉ ném lỗi nếu status code >= 500
        },
      });

      if (response.status === 200) {
        if (response.data.length > 0) {
          console.log(currentUser?.id);
          let array = response.data;
          array = array.filter((e) => {
            return e.id !== currentUser.id && e.email !== "admin@gmail.com";
          });
          setUserList(
            array.map((u) => {
              return {
                value: u.id,
                label: `${u.email}`,
              };
            })
          );
        }
      } else {
        setUserList([]);
        showSnackbar(response.data, "error");
      }
    } catch {
      showSnackbar("Lỗi", "error");
    }
  }, []);

  const isOptionSelected = (_, selectValue) => {
    return selectValue.length > 0;
  }; // set nếu đã chọn 2 value vào thẻ select r thì false , ko cho chọn nữa

  const connentToNewRecipient = async () => {
    if (userSelectRef !== undefined) {
      let recipients = [];

      userSelectRef.current.props.value?.forEach((u) =>
        recipients.push(u?.value)
      );

      if (recipients[0] > 0) {
        let response;
        try {
          response = await authAPI().post(
            endpoints["connentToNewRecipient"],
            {
              recipientId: recipients[0],
            },
            {
              validateStatus: function (status) {
                return status < 500;
              },
            }
          );
          if (response.status === 200) {
            showSnackbar("Kết nối thành công !", "success");
            setRecipient(response.data);
            getAllChatMessageBySenderAndRecipient(response.data);
            userSelectRef = undefined;
          } else {
            showSnackbar(response.data, "error");
            userSelectRef = undefined;
          }
        } catch {
          showSnackbar(response, "error");
          userSelectRef = undefined;
        }
      }
    }
    userSelectRef = undefined;
  };

  function truncateString(str) {
    if (str.length > 20) {
      return str.substring(0, 30) + "...";
    } else {
      return str;
    }
  }

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <div id="chatting-container" className="chatting-container">
        <div className="lock-icon">
          <button className="icon-lock-btn" onClick={toggleLockChatScreen}>
            <i
              id="lock-icon"
              class="fa-solid fa-lock"
              style={{ color: "#f50505" }}
            ></i>
          </button>
        </div>
        <div className="chatting-list shadow p-3">
          <div className="container mt-4 h-100">
            <div className="d-flex justify-content-between align-items-center">
              <div className="chat-header">Đoạn chat</div>
              {currentUser !== null && isBENHNHAN(currentUser) && (
                <button
                  className="btn btn-primary"
                  onClick={connentToConsultant}
                >
                  Kết nối với tư vấn viên
                </button>
              )}
            </div>
            <div className="mt-3">
              <div className="search-bar" onBlur={connentToNewRecipient}>
                <div className="search-icon">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    fill="currentColor"
                    className="bi bi-search"
                    viewBox="0 0 16 16"
                  >
                    <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.415l-3.85-3.85a1.007 1.007 0 0 0-.115-.098zm-5.344.856a5.5 5.5 0 1 1 0-11 5.5 5.5 0 0 1 0 11z" />
                  </svg>
                </div>
                <Select
                  isMulti
                  options={userList}
                  className="basic-multi-select fs-6"
                  classNamePrefix="select"
                  isSearchable={true}
                  ref={userSelectRef}
                  isOptionSelected={isOptionSelected}
                  placeholder="Tìm tài khoản muốn nhắn tin :"
                />
              </div>
            </div>
            <div className="recipient-list">
              {chatRooms !== null &&
                chatRooms?.length > 0 &&
                chatRooms.map((c) => {
                  return (
                    <>
                      <div
                        className={`recipient-items ${
                          recipient?.id === c.chatRoom.recipient.id
                            ? "active"
                            : ""
                        }`}
                        onClick={() =>
                          hanldeClickRecipientItem(c.chatRoom.recipient)
                        }
                      >
                        <div class="profile p-3">
                          <img
                            className="avatar"
                            src={c.chatRoom.recipient.avatar}
                            alt="Avatar"
                          />
                          <OnlineSide u={c.chatRoom.recipient} type="ICON" />
                          <div class="profile-info">
                            <h6>{c.chatRoom.recipient.name}</h6>
                            <small>
                              {truncateString(c.chatMessage.content)}
                            </small>
                          </div>
                        </div>
                      </div>
                    </>
                  );
                })}
            </div>
          </div>
        </div>
        <div className="chatting-content shadow container mt-3 p-3">
          {recipient !== null && (
            <div class="profile p-3 shadow-sm">
              <img src={recipient.avatar} alt="Avatar" />
              <OnlineSide u={recipient} type="ICON" />

              <div class="profile-info">
                <h6>{recipient.name}</h6>
                <OnlineSide u={recipient} type="TEXT" />
              </div>
            </div>
          )}
          {recipient !== null && (
            <div
              id="chatting-content-main"
              className="chatting-content-main h-100"
            >
              {renderMessages(messagesContainer, currentUser)}
            </div>
          )}
          {recipient !== null && (
            <form onSubmit={handleSubmitSendMessage}>
              <div className="box-sending shadow-sm">
                <div className="create-advice-container container mt-2">
                  <div className="comment-box">
                    <img className="avatar" src={currentUser?.avatar} />
                    <input
                      id="messageSendBox"
                      className="textbox"
                      role="textbox"
                      contenteditable="true"
                      aria-placeholder="Nhập văn bản tại đây"
                      data-placeholder="Nhập văn bản tại đây"
                      defaultValue={messageContent}
                      onChange={(e) => {
                        setMessageContent(e.target.value);
                      }}
                      required
                    />
                    <div className="comment-icons mt-2">
                      <button
                        type="submit"
                        className="btn btn-link p-0 text-light"
                      >
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="24"
                          height="24"
                          fill="currentColor"
                          className="bi bi-send text text-primary"
                          viewBox="0 0 16 16"
                        >
                          <path d="M15.854.146a.5.5 0 0 1 .11.53l-5 14a.5.5 0 0 1-.927.06L8.155 10.18l-6.792 2.264a.5.5 0 0 1-.65-.65l2.263-6.793L.262 1.036A.5.5 0 0 1 .32.109l14-5a.5.5 0 0 1 .534.037zm-13.5 7.982 5.149 1.721L14.561 1.44 2.354 8.128zm4.265 5.69 1.721 5.149L14.56 1.439 2.353 8.127z" />
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          )}
        </div>
      </div>
    </>
  );
}
