import "./Header.css";
import React, { useContext, useEffect, useRef, useState } from "react";
import LoginForm from "../LoginForm/LoginForm";
import RegisterForm from "../RegisterForm/RegisterForm";
import { UserContext } from "../config/Context";
import { Button, Dropdown, Image, NavDropdown } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { isBACSI, isBENHNHAN, isYTA } from "../Common/Common";
import NotificationContainer from "../NotificationContainer/NotificationContainer";
import { authAPI, endpoints } from "../config/Api";

export default function Header() {
  const formLoginRef = useRef();
  const formRegisterRef = useRef();
  const { currentUser, setCurrentUser } = useContext(UserContext);
  const navigate = useNavigate();

  function handleOpenLoginForm() {
    formLoginRef.current.open();
  }

  function handleCloseLoginForm() {
    formLoginRef.current.close();
  }

  function handleOpenRegisterForm() {
    formRegisterRef.current.open();
  }

  function handleCloseRegisterForm() {
    formRegisterRef.current.close();
  }

  const handleLogoutOnlineUser = async () => {
    let respone;
    try {
      respone = await authAPI().post(endpoints["logoutOnlineUser"], {
        validateStatus: function (status) {
          return status < 500;
        },
      });

      if (respone.status === 200) {
        localStorage.setItem("token", "");
        localStorage.setItem("HTML5_QRCODE_DATA", "");
        setCurrentUser(null);
        navigate("/");
      }
    } catch {}
  };

  useEffect(() => {}, [currentUser]);

  function handleAppointmentSchedule() {
    if (currentUser === null) {
      formLoginRef.current.open();
    }
  }

  return (
    <>
      <LoginForm ref={formLoginRef} onClose={handleCloseLoginForm} />
      <RegisterForm ref={formRegisterRef} onClose={handleCloseRegisterForm} />

      <div className="container-fluid topbar px-0 px-lg-4 bg-light py-2 d-none d-lg-block">
        <div className="container">
          <div className="row gx-0 align-items-center">
            <div className="col-lg-8 text-center text-lg-start mb-lg-0">
              <div className="d-flex flex-wrap">
                <div className="border-end border-success pe-3">
                  <a className="text-muted small">
                    <i className="fas fa-map-marker-alt text-success me-2"></i>
                    Xã Phước Kiển , Nhà Bè
                  </a>
                </div>
                <div className="ps-3">
                  <a className="text-muted small">
                    <i className="fas fa-envelope text-success me-2"></i>
                    2151050249manh@ou.edu.vn
                  </a>
                </div>
              </div>
            </div>
            <div className="col-lg-4 text-center text-lg-end">
              <div className="d-flex justify-content-end">
                <div className="d-flex border-end border-success pe-3">
                  <a className="btn p-0 text-success me-3">
                    <i className="fab fa-facebook-f"></i>
                  </a>
                  <a className="btn p-0 text-success me-3">
                    <i className="fab fa-telegram"></i>
                  </a>
                  <a className="btn p-0 text-success me-3">
                    <i className="fab fa-twitter"></i>
                  </a>
                  <a className="btn p-0 text-success me-3">
                    <i className="fab fa-instagram"></i>
                  </a>
                </div>
                {currentUser !== null && <NotificationContainer />}

                {currentUser !== null ? (
                  <>
                    <Dropdown className="dropdown text-end ms-2">
                      <Dropdown.Toggle className="btn btn-secondary">
                        <Image
                          src={currentUser === null ? "" : currentUser.avatar}
                          alt="mdo"
                          width="20"
                          height="20"
                          className="rounded-circle"
                        />
                      </Dropdown.Toggle>
                      <Dropdown.Menu className="r-0 header-dropdown-menu">
                        {currentUser !== null && (
                          <Dropdown.Item>
                            <Link
                              className="dropdown-item text-center"
                              to="/user-profile"
                            >
                              Thông tin cá nhân
                            </Link>
                          </Dropdown.Item>
                        )}
                        {currentUser !== null && isBENHNHAN(currentUser) && (
                          <Dropdown.Item>
                            <Link
                              className="dropdown-item text-center"
                              to="/user-register-schedule-list"
                            >
                              Danh sách lịch hẹn
                            </Link>
                          </Dropdown.Item>
                        )}

                        {currentUser !== null && isBENHNHAN(currentUser) && (
                          <Dropdown.Item>
                            <Link
                              className="dropdown-item text-center"
                              to="/history"
                            >
                              Lịch sử khám bệnh và hóa đơn
                            </Link>
                          </Dropdown.Item>
                        )}

                        {currentUser !== null && (
                          <Dropdown.Item>
                            <Link
                              className="dropdown-item text-center"
                              to="/chatting"
                            >
                              Nhắn tin tư vấn với chuyên gia
                            </Link>
                          </Dropdown.Item>
                        )}

                        {currentUser !== null && (
                          <Dropdown.Item>
                            <Link
                              className="dropdown-item text-center"
                              to="/chatting-to-AI"
                            >
                              Nhắn tin tư vấn với AI
                            </Link>
                          </Dropdown.Item>
                        )}

                        <NavDropdown.Divider />

                        <NavDropdown.Item>
                          <Button
                            className="dropdown-item text-center"
                            onClick={handleLogoutOnlineUser}
                          >
                            Đăng xuất
                          </Button>
                        </NavDropdown.Item>
                      </Dropdown.Menu>
                    </Dropdown>
                  </>
                ) : (
                  <div className="dropdown ms-3">
                    <a
                      className="dropdown-toggle text-dark"
                      data-bs-toggle="dropdown"
                    >
                      <small>
                        <i className="fas fa-user text-success me-2"></i> Tài
                        khoản
                      </small>
                    </a>
                    <div className="dropdown-menu rounded">
                      <a
                        onClick={handleOpenRegisterForm}
                        className="border border-white dropdown-item"
                      >
                        Đăng ký
                      </a>
                      <a
                        onClick={handleOpenLoginForm}
                        className="border border-white dropdown-item"
                      >
                        Đăng nhập
                      </a>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="container-fluid nav-bar px-0 px-lg-4 py-lg-0">
        <div className="container">
          <nav className="navbar navbar-expand-lg navbar-light">
            <Link to="/" className="navbar-brand p-0">
              <h1 className="text-success mb-0">HealthCare</h1>
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarCollapse"
            >
              <span className="fa fa-bars"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarCollapse">
              <div className="navbar-nav mx-0 mx-lg-auto">
                <a className="nav-item nav-link active">Trang chủ</a>
                <a className="nav-item nav-link">Giới thiệu</a>
                <a className="nav-item nav-link">Đội ngũ</a>
                <Link className="nav-item nav-link" to="/advise-section">
                  Tư vấn
                </Link>
                <div
                  className={
                    currentUser !== null && !isBENHNHAN(currentUser)
                      ? "nav-item dropdown"
                      : "nav-item"
                  }
                >
                  <a className="nav-link" data-bs-toggle="dropdown">
                    <span
                      className={
                        currentUser !== null && !isBENHNHAN(currentUser)
                          ? "dropdown-toggle"
                          : ""
                      }
                    >
                      Dịch vụ
                    </span>
                  </a>
                  <div className="dropdown-menu">
                  {currentUser !== null && isBENHNHAN(currentUser) && (
                      <>
                        <Link
                          to="/manage-time-exercise"
                          className="dropdown-item"
                        >
                          Quản lý thời gian tập thể dục
                        </Link>
                      </>
                    )}
                    {currentUser !== null && isYTA(currentUser) && (
                      <>
                        <Link
                          to="/directly-register-schedule"
                          className="dropdown-item"
                        >
                          Đặt lịch trực tiếp
                        </Link>
                      </>
                    )}
                    {currentUser !== null && isYTA(currentUser) && (
                      <>
                        <Link to="/censor-register" className="dropdown-item">
                          Duyệt danh sách khám
                        </Link>
                      </>
                    )}
                    {currentUser !== null && isYTA(currentUser) && (
                      <>
                        <Link
                          to="/qr-scan-take-order"
                          className="dropdown-item"
                        >
                          Quét mã QR lấy số thứ tự
                        </Link>
                      </>
                    )}
                    {currentUser !== null && isBACSI(currentUser) && (
                      <>
                        <Link
                          to="/prepare-examination-form"
                          className="dropdown-item"
                        >
                          Lập phiếu khám
                        </Link>
                      </>
                    )}
                  </div>
                </div>
                <a className="nav-item nav-link">Contact</a>
                <div className="nav-btn px-3">
                  <button className="btn-search btn btn-success btn-md-square rounded-circle flex-shrink-0">
                    <i className="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            <div className="d-none d-xl-flex flex-shrink-0 ps-4 ">
              <a
                className="btn btn-light btn-lg-square position-relative wow tada"
                data-wow-delay=".9s"
              >
                {currentUser !== null && isBENHNHAN(currentUser) ? (
                  <Link
                    to="/register-schedule"
                    className="d-flex flex-column text-center align-items-center border-0"
                  >
                    <i className="fa fa-calendar-alt fa-2x"></i>
                    <span className="text-blue">Đặt lịch khám</span>
                  </Link>
                ) : currentUser === null ? (
                  <button
                    onClick={handleAppointmentSchedule}
                    className="d-flex flex-column text-center align-items-center border-0"
                  >
                    <i className="fa fa-calendar-alt fa-2x"></i>
                    <span className="text-blue">Đặt lịch khám</span>
                  </button>
                ) : (
                  <></>
                )}
              </a>
            </div>
          </nav>
        </div>
      </div>
    </>
  );
}
