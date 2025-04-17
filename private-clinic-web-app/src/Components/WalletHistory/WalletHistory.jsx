import { forwardRef, useImperativeHandle, useRef } from "react";
import "./WalletHistory.css";

const WalletHistory = forwardRef(function WalletHistory({onCancel}, ref) {
  const dialog = useRef();

  useImperativeHandle(ref, () => {
    return {
      open() {
        dialog.current.style.border = "none";
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
        <div>
          <button
            className="btn-close position-absolute top-0 end-0 m-3"
            aria-label="Close"
            onClick={onCancel}
          ></button>
        </div>
      </dialog>
    </>
  );
});

export default WalletHistory;
