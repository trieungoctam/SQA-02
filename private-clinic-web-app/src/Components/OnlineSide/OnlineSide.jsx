import { useEffect, useState } from "react";
import { authAPI, endpoints } from "../config/Api";

export default function OnlineSide({ u, type }) {
  const [isOnline, setIsOnline] = useState(false);

  useEffect(() => {
    isUserOnline();
  });

  const [autoLoadComponent, setAutoLoadComponent] = useState(null);

  const updateData = () => {
    setAutoLoadComponent(new Date().toLocaleTimeString());
  };

  useEffect(() => {
    const interval = setInterval(() => {
      updateData();
    }, 100);
    return () => clearInterval(interval);
  }, []);

  const isUserOnline = async () => {
    let response;
    try {
      response = await authAPI().post(
        endpoints["isUserOnline"],
        {
          userId: u.id,
        },
        {
          validateStatus: function (status) {
            return status < 500;
          },
        }
      );
      if (response.status === 200) {
        setIsOnline(response.data);
      } else console.log(response, "error");
    } catch {
      console.log(response, "error");
    }
  };

  return (
    <>
      {isOnline === true ? (
        <>
          {type === "ICON" ? (
            <div class="status-indicator"></div>
          ) : (
            type === "TEXT" && <small>Online</small>
          )}
        </>
      ) : (
        <>{type === "TEXT" && <small>Offline</small>}</>
      )}
    </>
  );
}
