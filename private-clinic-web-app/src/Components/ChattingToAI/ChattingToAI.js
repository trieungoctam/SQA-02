import { useEffect, useState } from "react";
import "./ChattingToAI.css";
import { EventSource, EventSourcePolyfill } from "event-source-polyfill";
import ResultPrompt from "../ResultPrompt/ResultPrompt";
import Api, { BASE_URL, endpoints } from "../config/Api";
import { CustomerSnackbar } from "../Common/Common";

export default function ChattingToAI() {
  const [mainKeyword, setMainKeyword] = useState("");
  const [loading, setLoading] = useState(false);
  const [isFormValid, setIsFormValid] = useState(false);
  const [content, setContent] = useState("");

  const [open, setOpen] = useState(false);
  const [dataNotify, setDataNotify] = useState({
    message: "Thông báo mới",
    severity: "success",
  });

  const showSnackbar = (message, severity) => {
    setDataNotify({
      message: message,
      severity: severity,
    });

    setOpen(true);

    setTimeout(() => {
      setOpen(false);
    }, 4000);
  };

  const validateForm = () => {
    setIsFormValid(mainKeyword.trim());
  };

  // Gọi kiểm tra mỗi khi một trường thay đổi
  useEffect(() => {
    validateForm();
  }, [mainKeyword]);


  const handleCreateAdviseAnswer = async () => {
    setLoading(true);
    setContent(""); // Reset lại nội dung cũ trước khi tạo mới
    // const token = localStorage.getItem("token");

    try {
      const prompt = `Bạn là chuyên gia bác sĩ, hãy giải thích nguyên nhân và triệu chứng và đưa ra cách chữa trị từ khoá chính của loại bệnh là "${mainKeyword}". Câu trả lời đảm bảo chính xác, hữu ích, lịch sự và thân thiện với bệnh nhân.`;
      let eventSource = new EventSourcePolyfill(
        `${BASE_URL}/api/v1/coze/chat-stream?prompt=${encodeURIComponent(
          prompt
        )}&model=GPT-4o Mini`
      );

      eventSource.onopen = (event) => {
        console.log(event.target.readyState);
        console.log("connection opened");
      };

      eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        setContent((prev) => prev + data); // Dồn dữ liệu trả về
      };

      eventSource.onerror = (event) => {
        setLoading(false);
        eventSource.close();
        showSnackbar("AI đã tư vấn xong !", "success");
      };
    } catch (error) {
      console.error("Error creating advice answer !", error);
      setContent("Đã xảy ra lỗi khi tạo câu trả lời !");
      setLoading(false);
    }
  };

  const handleCreateAdviseAnswer2 = async () => {
    setLoading(true);
    setContent(""); // Reset lại nội dung cũ trước khi tạo mới
    // const token = localStorage.getItem("token");

    try {
      const prompt = `Bạn là chuyên gia bác sĩ, hãy giải thích nguyên nhân và triệu chứng và đưa ra cách chữa trị từ khoá chính của loại bệnh là ${mainKeyword}. Câu trả lời đảm bảo chính xác, hữu ích, lịch sự và thân thiện với bệnh nhân. Không được chứa bất kỳ ngôn ngữ nào khác ngoài Tiếng Việt.`;
      let eventSource = new EventSourcePolyfill(
        `${BASE_URL}/api/v1/hugging-face/completion-stream?prompt=${encodeURIComponent(
          prompt
        )}`
      );

      eventSource.onopen = (event) => {
        console.log(event.target.readyState);
        console.log("connection opened");
      };

      eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        setContent((prev) => prev + data); // Dồn dữ liệu trả về
      };

      eventSource.onerror = (event) => {
        setLoading(false);
        eventSource.close();
        showSnackbar("AI đã tư vấn xong !", "success");
      };
    } catch (error) {
      console.error("Error creating advice answer !", error);
      setContent("Đã xảy ra lỗi khi tạo câu trả lời !");
      setLoading(false);
    }
  };

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={dataNotify.message}
        severity={dataNotify.severity}
      />
      <div className="advise-answer-container">
        <h1 className="header">Tư vấn lời khuyên từ AI</h1>

        <div className="form">
          {/* Input group for main keyword */}
          <div className="inputGroup">
            <label htmlFor="mainKeyword">Từ khoá chính(*)</label>
            <input
              type="text"
              id="mainKeyword"
              placeholder="Nhập tên loại bệnh"
              value={mainKeyword}
              onChange={(e) => setMainKeyword(e.target.value)}
            />
          </div>

          <div className="d-flex">
            <button
              className="button"
              onClick={() => handleCreateAdviseAnswer()}
              disabled={!isFormValid || loading}
            >
              {loading
                ? "Đang đưa ra câu trả lời ..."
                : "Nhận tư vấn từ AI thứ nhất"}
            </button>

            <button
              className="button"
              onClick={() => handleCreateAdviseAnswer2()}
              disabled={!isFormValid || loading}
            >
              {loading
                ? "Đang đưa ra câu trả lời ..."
                : "Nhận tư vấn từ AI thứ 2"}
            </button>
          </div>
        </div>
        <ResultPrompt content={content} loading={loading} />
      </div>
    </>
  );
}
