import { forwardRef, useImperativeHandle, useRef } from "react";
import "./OrderPDF.css";
import dayjs from "dayjs";

const OrderPDF = forwardRef(function OrderPDF({ orderQrCode }, ref) {
  const dialog = useRef();

  useImperativeHandle(ref, () => {
    return {
      open() {
        dialog.current.style.border = "none";
        dialog.current.style.background = "white";
        dialog.current.showModal();
      },

      close() {
        dialog.current.close();
      },
    };
  });

  return (
    <>
      <dialog ref={dialog}>
        <div class="ticket-container">
          <div class="header">PHÒNG KHÁM TƯ NHÂN</div>
          <div class="date">
            <span id="current-date">
              {dayjs(orderQrCode.registerDate).format("DD-MM-YYYY")}
            </span>
          </div>
          <div>
            <span id="name-phone">
              {orderQrCode.name} - {orderQrCode.phone}
            </span>
          </div>
          <div class="ticket-number">{orderQrCode.order}</div>
          <div class="footer">
            Quý Khách vui lòng chờ tới số thứ tự.
            <br />
            XIN CẢM ƠN!
          </div>
        </div>
      </dialog>
    </>
  );
});

export default OrderPDF;
