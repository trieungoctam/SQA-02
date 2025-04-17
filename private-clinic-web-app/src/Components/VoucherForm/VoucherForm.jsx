import { forwardRef, useImperativeHandle, useRef } from "react";
import "./VoucherForm.css";
import { CustomerSnackbar } from "../Common/Common";

const VoucherForm = forwardRef(function VoucherForm(
  {
    onCancel,
    handleApplyVoucher,
    setCode,
    open,
    data,
  },
  ref
) {
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
      <dialog className="voucher-dialog" ref={dialog}>
        <CustomerSnackbar
          open={open}
          message={data.message}
          severity={data.severity}
        />
        <div className="voucher-container">
          <button
            className="btn-close position-absolute top-0 end-0 m-3"
            aria-label="Close"
            onClick={onCancel}
          ></button>
          <div className="modal-header">
            <h5 className="modal-title">Chọn Voucher</h5>
          </div>
          <div className="modal-body">
            <div className="voucher-search d-flex align-items-center justify-content-center">
              <label placeholder="Mã Voucher" className="label-search me-2">
                Mã voucher{" "}
              </label>
              <input
                type="text"
                placeholder="Mã HealthCare Voucher"
                className="form-control me-2"
                onChange={(e) => setCode(e.target.value)}
              />
              <button
                className="btn btn-success apply-button"
                onClick={handleApplyVoucher}
              >
                ÁP DỤNG
              </button>
            </div>
            <h6 className="mt-3">Mã giảm giá phí dịch vụ khám bệnh</h6>
            <p className="text-muted">Có thể chọn 1 Voucher</p>

            {/* <div className="voucher-list">
              <div className="voucher-item p-2 mb-3 border rounded">
                <div className="d-flex">
                  <div className="voucher-icon">
                    <img
                      src="voucher-icon.png"
                      alt="Voucher Icon"
                      className="img-fluid"
                    />
                  </div>
                  <div className="voucher-details ms-3">
                    <h6>Giảm tối đa ₫55k</h6>
                    <p>Đơn Tối Thiểu ₫0</p>
                    <span className="badge bg-danger text-white">
                      Dành riêng cho bạn
                    </span>
                    <p className="text-muted">
                      HSD: 05.09.2024 <a href="#">Điều Kiện</a>
                    </p>
                  </div>
                  <div className="ms-auto">
                    <input
                      type="radio"
                      name="voucher"
                      className="form-check-input"
                    />
                  </div>
                </div>
                <p className="mt-2 alert alert-warning p-2">
                  Vui lòng mua hàng trên ứng dụng Shopee để sử dụng ưu đãi.
                </p>
              </div>

              <div className="voucher-item p-2 mb-3 border rounded">
                <div className="d-flex">
                  <div className="voucher-icon">
                    <img
                      src="voucher-icon.png"
                      alt="Voucher Icon"
                      className="img-fluid"
                    />
                  </div>
                  <div className="voucher-details ms-3">
                    <h6>Giảm tối đa ₫30k</h6>
                    <p>Đơn Tối Thiểu ₫0</p>
                    <span className="badge bg-danger text-white">
                      Dành riêng cho bạn
                    </span>
                    <p className="text-muted">HSD: 05.09.2024</p>
                  </div>
                  <div className="ms-auto">
                    <input
                      type="radio"
                      name="voucher"
                      className="form-check-input"
                    />
                  </div>
                </div>
                <p className="mt-2 alert alert-warning p-2">
                  Vui lòng mua hàng trên ứng dụng Shopee để sử dụng ưu đãi.
                </p>
              </div>
            </div> */}
          </div>
          {/* <div className="modal-footer">
            <button className="btn btn-secondary">TRỞ LẠI</button>
            <button className="btn btn-danger">OK</button>
          </div> */}
        </div>
      </dialog>
    </>
  );
});

export default VoucherForm;
