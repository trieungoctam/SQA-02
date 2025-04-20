import { useContext, useEffect, useRef, useState } from "react";
import {
  Button,
  Col,
  Container,
  Form,
  Image,
  InputGroup,
  Row,
  Stack,
} from "react-bootstrap";
import { CustomerSnackbar } from "../Common/Common";
import { UserContext } from "../config/Context";
import { authAPI, endpoints } from "../config/Api";
import dayjs from "dayjs";
import { Alert, CircularProgress, Pagination } from "@mui/material";

export default function UserProfile() {
  const { currentUser, fetchUser } = useContext(UserContext);

  const [src, setSrc] = useState(null);
  const imageRef = useRef(null);

  const [profile, setProfile] = useState({
    name: "",
    address: "",
    birthday: null,
  });

  const [changePassword, setChangePassword] = useState({
    oldPassword: "",
    newPassword: "",
  });

  const [loading, setLoading] = useState(false);

  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Thay đổi ảnh đại diện thành công",
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
    }, 2000);
  };

  useEffect(() => {
    document.title = "Thông tin cá nhân";
  }, [profile, src, open, currentUser]);

  const changeAvatar = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();

      reader.readAsDataURL(file);

      reader.onload = (e) => {
        setSrc(e.target.result);
      };
    }
    setSrc(null);
  };

  const handleSubmitChangeAvatar = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      let form = new FormData();
      form.append("avatar", imageRef.current.files[0]);

      const response = await authAPI().patch(endpoints["changeAvatar"], form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      if (response.status === 200) {
        showSnackbar("Thay đổi ảnh đại diện thành công", "success");
        fetchUser();
      }
      setLoading(false);
    } catch {
      showSnackbar("Thay đổi ảnh đại diện thất bại", "error");
      setLoading(false);
    }
    setLoading(false);
  };

  function handleAutoFillUpdateProfileFormAtt(e) {
    const { name, value } = e.target;
    setProfile((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  function handleAutoFillChangePasswordFormAtt(e) {
    const { name, value } = e.target;
    setChangePassword((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  const handleUpdateProfile = async (e) => {
    e.preventDefault();

    let response;
    if (currentUser !== null) {
      try {
        response = await authAPI().patch(
          endpoints["updateProfile"],
          {
            ...profile,
          },
          {
            validateStatus: function (status) {
              return status < 500;
            },
          }
        );
        if (response.status === 200) {
          showSnackbar("Cập nhật thông tin thành công !", "success");
        } else showSnackbar(response.data, "error");
      } catch {}
    } else {
      showSnackbar("Bạn cần đăng nhập để thay đổi thông tin !", "error");
    }
  };

  const handleSubmitChangePassword = async (e) => {
    e.preventDefault();

    let response;
    if (currentUser !== null) {
      try {
        response = await authAPI().patch(
          endpoints["changePassword"],
          {
            ...changePassword,
          },
          {
            validateStatus: function (status) {
              return status < 500;
            },
          }
        );
        if (response.status === 200) {
          showSnackbar("Thay đổi mật khẩu thành công !", "success");
          setChangePassword((prev) => ({
            oldPassword: "",
            newPassword: "",
          }));
          document.getElementById("oldPassword").value = "";
          document.getElementById("newPassword").value = "";
        } else showSnackbar(response.data, "error");
      } catch {
        document.getElementById("oldPassword").value = "";
        document.getElementById("newPassword").value = "";
      }
    } else {
      showSnackbar("Bạn cần đăng nhập để thay đổi mật khẩu !", "error");
    }
  };

  return (
    <>
      {currentUser === null ? (
        <></>
      ) : (
        <>
          <CustomerSnackbar
            open={open}
            message={data.message}
            severity={data.severity}
          />

          <Stack>
            <Container>
              <h1 className="text-center text-success my-4">
                Thông tin cá nhân
              </h1>

              <div className="d-flex justify-content-center">
                <div>
                  <Image
                    src={currentUser.avatar}
                    width="200"
                    height="200"
                    alt="Ảnh đại diện"
                    roundedCircle
                    className="my-4 mx-auto"
                  />
                </div>

                {loading ? (
                  <>
                    <div className="d-flex justify-content-center align-item-center">
                      <CircularProgress className="mt-3" />
                    </div>
                  </>
                ) : (
                  <div className="d-flex align-items-center ms-4">
                    <Form onSubmit={handleSubmitChangeAvatar}>
                      <Form.Group controlId="formFile" className="mb-3">
                        <Form.Label>Chọn ảnh</Form.Label>
                        <Form.Control
                          type="file"
                          accept=".jsp, .png"
                          ref={imageRef}
                          onChange={(e) => changeAvatar(e)}
                          required
                        />
                      </Form.Group>

                      <Button type="submit">Lưu ảnh đại diện thay đổi</Button>
                    </Form>
                  </div>
                )}
              </div>
            </Container>

            <Container fluid className="w-50 mx-auto my-4 shadow-sm pb-3">
              <Form className="needs-validation" onSubmit={handleUpdateProfile}>
                <Row className="mb-3">
                  <Col>
                    <Form.Group controlId="name">
                      <Form.Label>Tên</Form.Label>
                      <Form.Control
                        type="text"
                        defaultValue={currentUser.name}
                        name="name"
                        required
                        onChange={handleAutoFillUpdateProfileFormAtt}
                      />
                    </Form.Group>
                  </Col>

                  <Col>
                    <Form.Group controlId="role">
                      <Form.Label>Vai trò</Form.Label>
                      <Form.Control
                        type="text"
                        required
                        disabled
                        value={currentUser.role.name}
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Row className="mb-3">
                  <Form.Group controlId="address" className="col-sm-6">
                    <Form.Label>Địa chỉ</Form.Label>
                    <Form.Control
                      type="text"
                      defaultValue={currentUser.address}
                      name="address"
                      onChange={handleAutoFillUpdateProfileFormAtt}
                      required
                    />
                  </Form.Group>

                  <Form.Group controlId="birthday" className="col-sm-6">
                    <Form.Label>Ngày sinh</Form.Label>
                    <Form.Control
                      type="date"
                      defaultValue={dayjs(currentUser.birthday).format(
                        "YYYY-MM-DD"
                      )}
                      name="birthday"
                      max={dayjs(new Date()).format("YYYY-MM-DD")}
                      onChange={handleAutoFillUpdateProfileFormAtt}
                      required
                    />
                  </Form.Group>
                </Row>

                <Row className="mb-3">
                  <Form.Group controlId="userId" className="col-sm-6">
                    <Form.Label>Mã người dùng</Form.Label>
                    <Form.Control
                      type="text"
                      required
                      disabled
                      value={currentUser.id}
                    />
                  </Form.Group>

                  <Form.Group controlId="phone" className="col-sm-6">
                    <Form.Label>Phone</Form.Label>
                    <InputGroup>
                      <InputGroup.Text>84</InputGroup.Text>
                      <Form.Control
                        type="number"
                        value={currentUser.phone}
                        disabled
                      />
                    </InputGroup>
                  </Form.Group>
                </Row>

                <Row className="mb-3">
                  <Form.Group controlId="email" className="col-sm-6">
                    <Form.Label>Email</Form.Label>
                    <InputGroup>
                      <InputGroup.Text>@</InputGroup.Text>
                      <Form.Control
                        type="text"
                        required
                        disabled
                        value={currentUser.email}
                      />
                    </InputGroup>
                  </Form.Group>
                  <div className="col-sm-6 d-flex justify-content-center align-items-end">
                    <Button type="submit">Lưu thay đổi thông tin</Button>
                  </div>
                </Row>
              </Form>
            </Container>
            <Container fluid className="w-50 mx-auto my-4 shadow-sm pb-3">
              <Form onSubmit={handleSubmitChangePassword}>
                <Form.Label className="text text-danger text-center m-2 fs-5">
                  ĐỔI MẬT KHẨU
                </Form.Label>
                <Row className="mb-3 mt-3">
                  <Col>
                    <Form.Group controlId="oldPassword">
                      <Form.Label>Mật khẩu cũ</Form.Label>
                      <Form.Control
                        type="password"
                        name="oldPassword"
                        id="oldPassword"
                        required
                        defaultValue={changePassword.oldPassword}
                        onChange={handleAutoFillChangePasswordFormAtt}
                      />
                    </Form.Group>
                  </Col>

                  <Col>
                    <Form.Group controlId="newPassword">
                      <Form.Label>Mật khẩu mới</Form.Label>
                      <Form.Control
                        type="password"
                        required
                        name="newPassword"
                        id="newPassword"
                        defaultValue={changePassword.newPassword}
                        onChange={handleAutoFillChangePasswordFormAtt}
                      />
                    </Form.Group>
                  </Col>
                </Row>
                <div className="d-flex justify-content-center align-items-center">
                  <Button className="btn btn-danger" type="submit">
                    Lưu mật khẩu mới
                  </Button>
                </div>
              </Form>
            </Container>
          </Stack>
        </>
      )}
    </>
  );
}
