import { forwardRef, useImperativeHandle, useRef } from "react";
import "./DeleteConfirmationForm.css";

const DeleteConfirmationForm = forwardRef(function DeleteConfirmationForm(
  {onDelete , onCancel},
  ref
) {
  const dialog = useRef();

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
  return (
    <>
      <dialog ref={dialog}>
        <div className="warning-popup">
          <div className="warning-icon">⚠️</div>
          <h2>Bạn có chắc chắn muốn hủy lịch không ?</h2>
          <p>
            Sau khi hủy lịch , nếu muốn tái khám vui lòng đăng kí lịch hẹn mới và chờ xác nhận từ phía y tá
          </p>
          <div className="buttons">
            <button className="delete-button" onClick={() => onDelete()}>
              Xác nhận
            </button>
            <button className="cancel-button" onClick={onCancel}>
              Hủy
            </button>
          </div>
        </div>
      </dialog>
    </>
  );
});

export default DeleteConfirmationForm;
