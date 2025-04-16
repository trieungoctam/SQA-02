import React, { useCallback, useEffect, useRef, useState } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";
import { CustomerSnackbar } from "../Common/Common";
import Api, { endpoints } from "../config/Api";
import OrderPDF from "../OrderPDF/OrderPDF";

const QRScanner = () => {
  let decodeTextNowRef = useRef(null);
  let lastResultRef = useRef("diff");
  let isFinishedRef = useRef(true);

  const orderPdfRef = useRef();

  const [orderQrCode, setOrderQrCode] = useState({
    order: "",
    name: "",
    phone: "",
    registerDate: "",
  });

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
    }, 7000);
  };

  function domReady(fn) {
    if (
      document.readyState === "complete" ||
      document.readyState === "interactive"
    ) {
      // nếu đã load đc camera
      setTimeout(fn, 1000);
    } else {
      document.addEventListener("DOMContentLoaded", fn);
    }
  }

  const loadTakeOrderFromQrCode = useCallback(async () => {
    const response = await Api.post(
      endpoints["takeOrderFromQrCode"],
      {
        mrlId: decodeTextNowRef.current,
      },
      {
        validateStatus: function (status) {
          return status < 500; // Chỉ ném lỗi nếu status code >= 500
        },
      }
    );
    if (response.status === 200) {
      showSnackbar("Quét mã QR lấy số thứ tự thành công!", "success");
      orderPdfRef.current.open(); // mở bảng thông báo số thứ tự
      setOrderQrCode((prev) => ({
        order: response.data.order,
        name: response.data.name,
        phone: response.data.phone,
        registerDate: response.data.registerDate,
      }));
      // setTimeOut để tránh QR bị quét quá nhanh dẫn đến quét trùng kết quả
      setTimeout(() => {
        decodeTextNowRef.current = null; // set null để báo hiệu là đã xong phiên quét hiện tại
        isFinishedRef.current = true;
        orderPdfRef.current.close();
      }, 7000);
    } else {
      showSnackbar(response.data, "error");
      setTimeout(() => {
        decodeTextNowRef.current = null;
        isFinishedRef.current = true;
      }, 7000);
    }
  }, []); // đã fecth lấy dữ liệu đc , ko cần nạp lại decodeTextNowRef

  useEffect(() => {
    domReady(() => {
      function onScanSuccess(decodeText, decodeResult) {
        // phải có 1 cờ isFinishedRef để ngăn chặn việc quét quá nhanh
        if (isFinishedRef.current === true) {
          isFinishedRef.current = false; // set về false để làm cờ bắt đầu 1 phiên quét mới
          // nếu như kết quả quét trước khác với kết quả quét hiện tại
          if (decodeText !== lastResultRef.current) {
            // check cờ decodeTextNowRef bắt đầu kết quả quét của phiên luôn luôn là null
            if (decodeTextNowRef.current === null) {
              lastResultRef.current = decodeText;
              decodeTextNowRef.current = decodeText;
              loadTakeOrderFromQrCode();
            }
          } else {
            showSnackbar("Mã QR này đã được quét !", "error");
            isFinishedRef.current = true;
            decodeTextNowRef.current = null
          }
        }
      }

      const htmlScanner = new Html5QrcodeScanner("my-qr-reader", {
        fps: 60,
        qrbox: 500,
      });

      // lấy quyền và id máy bậy camera lên từ localStorage

      const HTML5_QRCODE_DATA = JSON.parse(
        localStorage.getItem("HTML5_QRCODE_DATA")
      );
      const hasPermission = HTML5_QRCODE_DATA.hasPermission;
      const lastUsedCameraId = HTML5_QRCODE_DATA.lastUsedCameraId;
      // nếu camera chưa được bật lần nào sau khi nạp
      if (hasPermission === false && lastUsedCameraId === null) {
        htmlScanner.render(onScanSuccess);
        decodeTextNowRef.current = null;
        // nếu camera đã được bật ít nhất 1 lần
      } else if (hasPermission === true && lastUsedCameraId !== null) {
        HTML5_QRCODE_DATA.hasPermission = false;
        HTML5_QRCODE_DATA.lastUsedCameraId = null;

        localStorage.setItem(
          "HTML5_QRCODE_DATA",
          JSON.stringify({
            HTML5_QRCODE_DATA,
          })
        );
        htmlScanner.render(onScanSuccess);
        decodeTextNowRef.current = null;
        isFinishedRef.current = true;
      }
    });
  }, []); // (decodeTextNowRef ko cần đặt trong depend)

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <OrderPDF ref={orderPdfRef} orderQrCode={orderQrCode} />
      <div>
        <div id="your-qr-result"></div>
        <div style={{ display: "flex", justifyContent: "center" }}>
          <div id="my-qr-reader" style={{ width: "200%" }}></div>
        </div>
      </div>
    </>
  );
};

export default QRScanner;
