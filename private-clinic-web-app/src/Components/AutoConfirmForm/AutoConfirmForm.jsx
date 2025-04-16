import {
  forwardRef,
  useCallback,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from "react";
import "./AutoConfirmForm.css";
import Select from "react-select";
import { authAPI, endpoints } from "../config/Api";
import { CustomerSnackbar } from "../Common/Common";
import { CircularProgress } from "@mui/material";

const AutoConfirmForm = forwardRef(function AutoConfirmForm(
  { onClose, statusList, setIsConfirmRegister },
  ref
) {
  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Nạp dữ liệu thành công",
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

  const dialog = useRef();

  let userSelectRef = useRef(undefined);

  const [autoRegisterDate, setAutoRegisterDate] = useState("");
  const [autoStatus, setAutoStatus] = useState("PAYMENTPHASE1");
  const [userList, setUserList] = useState([]);

  const [emailContent, setEmailContent] = useState("");

  const [loading, setLoading] = useState();

  useImperativeHandle(ref, () => {
    return {
      open() {
        dialog.current.style.border = "none";
        dialog.current.style.background = "none";
        dialog.current.showModal();
      },

      close() {
        dialog.current.close();
      },
    };
  });

  // const isOptionSelected = (_, selectValue) => {
  // 	return selectValue.length > 1;
  // }; // set nếu đã chọn 2 value vào thẻ select r thì false , ko cho chọn nữa

  const getAllUsersByScheduleAndStatus = useCallback(async () => {
    let response;
    try {
      response = await authAPI().post(
        endpoints["getUsersByRegisterDateAndStatus"],
        {
          registerDate: autoRegisterDate,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );

      if (response.status === 200) {
        if (response.data.length > 0)
          setUserList(
            response.data.map((u) => {
              return {
                value: u.email,
                label: `${u.email}`,
              };
            })
          );
      } else {
        setUserList([]);
        showSnackbar(response.data, "error");
      }
    } catch {
      showSnackbar("Lỗi", "error");
    }
  }, [autoRegisterDate]);

  useEffect(() => {
    if (autoRegisterDate !== "") getAllUsersByScheduleAndStatus();
  }, [autoRegisterDate]);

  const ytaAutoConfirmRegister = async (event) => {
    event.preventDefault();
    setLoading(true);

    let emails = [];
    if (
      userSelectRef.current !== undefined &&
      userSelectRef.current.props.value !== null
    )
      userSelectRef.current.props.value.forEach((u) => emails.push(u.value));

    let response;
    try {
      response = await authAPI().post(
        endpoints["ytaAutoConfirmRegister"],
        {
          status: autoStatus,
          registerDate: autoRegisterDate,
          emails: emails,
          emailContent: emailContent,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );

      if (response.status === 200) {
        showSnackbar(
          "Gửi email trả lời xác nhận đặt lịch khám thành công !",
          "success"
        );
        setUserList([]);
        setAutoRegisterDate("");
        userSelectRef.current = undefined;
        dialog.current.close();
        setIsConfirmRegister(true);
      } else {
        showSnackbar(response.data, "error");
        userSelectRef.current = undefined;
      }
    } catch {
      showSnackbar("Lỗi", "error");
      userSelectRef.current = undefined;
      console.log(response);
    }
    setTimeout(() => {
      setLoading(false);
    }, 2400);
    userSelectRef.current = undefined;
    setLoading(false);
  };

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <dialog ref={dialog}>
        <div className="auto-form-container">
          <div onClick={onClose} className="close-button">
            X
          </div>
          <form onSubmit={(e) => ytaAutoConfirmRegister(e)}>
            <div className="auto-form-group">
              <div className="auto-form-control-group">
                <label id="lableStatusSelect" htmlFor="statusSelect">
                  Trạng thái
                </label>
                <select
                  id="statusSelect"
                  required
                  className="form-control"
                  value={autoStatus}
                  onChange={(e) => setAutoStatus(e.target.value)}
                >
                  {statusList.length > 0 &&
                    statusList.map((s) => {
                      return (
                        <>
                          {(s.status === "FAILED" ||
                            s.status === "PAYMENTPHASE1") && (
                            <option key={s.id} value={s.status}>
                              <strong className="text-white">{s.status}</strong>
                            </option>
                          )}
                        </>
                      );
                    })}
                </select>
              </div>
              <div className="auto-form-control-group">
                <label htmlFor="dateInput">Chọn ngày</label>
                <input
                  id="dateInput"
                  type="date"
                  className="form-control"
                  value={autoRegisterDate}
                  onChange={(e) => setAutoRegisterDate(e.target.value)}
                  required
                />
              </div>
            </div>
            <label htmlFor="textarea">
              Chọn email để gửi riêng (Không bắt buộc)
            </label>
            <Select
              isMulti
              name="users"
              options={userList}
              className="basic-multi-select fs-6 mb-3"
              classNamePrefix="select"
              // isOptionSelected={isOptionSelected}
              isSearchable={true}
              placeholder="Chọn email để gửi riêng nếu cần"
              ref={userSelectRef}
            />
            <div className="auto-form-group">
              <label htmlFor="textarea">Nội dung mail :</label>
              <textarea
                required
                id="textarea"
                className="form-control"
                value={emailContent}
                onChange={(e) => setEmailContent(e.target.value)}
              ></textarea>
            </div>
            {loading ? (
              <div className="d-flex justify-content-center align-item-center">
                <CircularProgress className="mt-3" />
              </div>
            ) : (
              <button type="submit" className="submit-button">
                Gửi xác nhận lịch khám
              </button>
            )}
          </form>
        </div>
      </dialog>
    </>
  );
});

export default AutoConfirmForm;
