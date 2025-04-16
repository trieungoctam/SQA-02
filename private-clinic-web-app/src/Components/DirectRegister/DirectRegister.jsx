import { useCallback, useEffect, useRef, useState } from "react";
import "./DirectRegister.css";
import { CustomerSnackbar } from "../Common/Common";
import { CircularProgress } from "@mui/material";
import { authAPI, endpoints } from "../config/Api";
import Select from "react-select";
import { useNavigate } from "react-router-dom";

export default function DirectRegister() {
  const [directRegisterState, setDirectRegisterState] = useState({
    name: "",
    email: "",
    favor: "",
  });
  const [loading, setLoading] = useState();

  const navigate = useNavigate();

  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Đặt lịch thành công",
    severity: "success",
  });

  const [userList, setUserList] = useState([]);
  let userSelectRef = useRef(undefined);

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

  const isOptionSelected = (_, selectValue) => {
    return selectValue.length > 0;
  }; // set nếu đã chọn 2 value vào thẻ select r thì false , ko cho chọn nữa

  function hanldeRegisterScheduleState(e) {
    const { name, value } = e.target;
    setDirectRegisterState((prev) => ({
      ...prev,
      [name]: value,
    }));
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
  }, []);

  useEffect(() => {
    if (userList.length < 1) getAllUsers();
  }, []);

  const directScheduleAct = async (event) => {
    event.preventDefault();

    setLoading(true);

    try {
      let emails = [];
      if (userSelectRef.current !== undefined)
        userSelectRef.current.props.value.forEach((u) => emails.push(u.value));

      const response = await authAPI().post(
        endpoints["directRegister"],
        {
          ...directRegisterState,
          email: emails[0],
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );

      if (response.status === 201) {
        showSnackbar(response.data, "success");
      } else {
        showSnackbar(response.data, "error");
      }
    } catch {
      showSnackbar("Lỗi", "error");
    }
    setTimeout(() => {
      setLoading(false);
      navigate("/censor-register");
    }, 2400);
  };
  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />

      <div className="appointment-form-container">
        <div className="appointment-form">
          <h2 className="text text-primary">Đặt Lịch trực tiếp</h2>
          <form id="appointmentForm" onSubmit={directScheduleAct}>
            <div className="form-group">
              <label htmlFor="name">Tên người khám</label>
              <input
                onChange={hanldeRegisterScheduleState}
                type="text"
                id="name"
                name="name"
                value={directRegisterState.name}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="date">Email đặt lịch khám : </label>
              <Select
                isMulti
                options={userList}
                className="basic-multi-select fs-6 mb-3"
                classNamePrefix="select"
                isOptionSelected={isOptionSelected}
                isSearchable={true}
                placeholder="Email đặt lịch khám"
                ref={userSelectRef}
              />
            </div>
            <div className="form-group">
              <label htmlFor="favor">Mô tả triệu chứng</label>
              <textarea
                onChange={hanldeRegisterScheduleState}
                id="favor"
                name="favor"
                rows="4"
                value={directRegisterState.favor}
                required
              ></textarea>
            </div>

            {loading ? (
              <>
                <div className="d-flex justify-content-center align-item-center">
                  <CircularProgress className="mt-3" />
                </div>
              </>
            ) : (
              <button type="submit">Đăng kí lấy mã QR</button>
            )}
          </form>
        </div>
      </div>
    </>
  );
}
