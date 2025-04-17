import { useEffect, useState } from "react";
import { authAPI, endpoints } from "../config/Api";

export default function LastChatMessage({ r }) {
  const [content, setContent] = useState("");

  useEffect(() => {
    getLastChatMessage();
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

  const getLastChatMessage = async () => {
    let response;
    try {
      response = await authAPI().post(
        endpoints["getLastChatMessage"],
        {
          recipientId: r.id,
        },
        {
          validateStatus: function (status) {
            return status < 500;
          },
        }
      );
      if (response.status === 200) {
        setContent(response.data.content);
      } else if (response.status === 204) {
        setContent("");
      } else console.log(response, "error");
    } catch {
      console.log(response, "error");
    }
  };

  function truncateString(str) {
    if (str.length > 8) {
      return str.substring(0, 30) + '...';
    } else {
      return str;
    }
  }

  return (
    <>
      <small>{truncateString(content)}</small>
    </>
  );
}
