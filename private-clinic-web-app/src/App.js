import { Fragment, useContext, useEffect, useState } from "react";
import "./App.css";
import Header from "./Components/Header/Header";
import {
  SnackbarContextProvider,
  SnackbarProvider,
  UserContext,
  UserContextProvider,
} from "./Components/config/Context";
import { authAPI, endpoints } from "./Components/config/Api";
import Footer from "./Components/Footer/Footer";
import AppointmentForm from "./Components/AppointmentForm/AppointmentForm";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { CustomerSnackbar, isBENHNHAN } from "./Components/Common/Common";
import DefaultLayout from "./Components/DefaultLayout/DefaultLayout";
import { publicRoutes } from "./Components/Routes/Routes";

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [token, setToken] = useState("");
  const [BENHNHANnotifications, setBENHNHANNotifications] = useState([]);

  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Đăng nhập thành công",
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

  const fetchUser = async () => {
    let response;
    try {
      response = await authAPI().get(endpoints["currentUser"], {
        validateStatus: function (status) {
          return status < 500; // Chỉ ném lỗi nếu status code >= 500
        },
      });
      if (response.status === 200) {
        setCurrentUser(response.data);
        setToken(localStorage.getItem("token"));
      } else {
        showSnackbar(
          "Phiên đăng nhập đã quá hạn , vui lòng đăng nhập lại",
          "error"
        );
        localStorage.setItem("token", "");
        localStorage.setItem("HTML5_QRCODE_DATA", "");
        setCurrentUser(null);
      }
    } catch {
      showSnackbar(
        "Phiên đăng nhập đã quá hạn , vui lòng đăng nhập lại",
        "error"
      );
    }
  };

  useEffect(() => {
    if (localStorage.getItem("token")) {
      fetchUser();
    }
  }, []);

  const userCtx = {
    currentUser: currentUser,
    setCurrentUser: setCurrentUser,
    token: token,
    fetchUser : fetchUser,    
    BENHNHANnotifications: BENHNHANnotifications,
    setBENHNHANNotifications : setBENHNHANNotifications,
  };

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <BrowserRouter>
        <UserContext.Provider value={userCtx}>
          <div className="App">
            <Routes>
              {publicRoutes.map((route, index) => {
                const Page = route.component;

                let Layout = DefaultLayout;

                if (route.layout) {
                  Layout = route.layout;
                } else if (route.layout === null) {
                  Layout = Fragment;
                }
                
                if (currentUser === null && route.role !== "ROLE_ALL")
                  return <></>;
                else if (currentUser !== null && route.role !== "ROLE_ALL") {
                  if (currentUser?.role?.name === route.role) {
                    
                    return (
                      <Route
                        key={index}
                        path={route.path}
                        element={
                          <Layout>
                            <Page />
                          </Layout>
                        }
                      />
                    );
                  }
                  if (route.role === "ROLE_ANY") {
                    return (
                      <Route
                        key={index}
                        path={route.path}
                        element={
                          <Layout>
                            <Page />
                          </Layout>
                        }
                      />
                    );
                  }
                } else if (
                  (currentUser === null && route.role === "ROLE_ALL") ||
                  (currentUser !== null && route.role === "ROLE_ALL")
                ) {
                  return (
                    <Route
                      key={index}
                      path={route.path}
                      element={
                        <Layout>
                          <Page />
                        </Layout>
                      }
                    />
                  );
                } else {
                  <></>;
                }
              })}
            </Routes>
          </div>
        </UserContext.Provider>
      </BrowserRouter>
    </>
  );
}

export default App;
