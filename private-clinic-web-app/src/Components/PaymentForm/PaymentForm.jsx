import {
  forwardRef,
  useContext,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from "react";
import "./PaymentForm.css";
import dayjs from "dayjs";
import { authAPI, endpoints } from "../config/Api";
import { CustomerSnackbar, isBENHNHAN, isYTA } from "../Common/Common";
import { CircularProgress } from "@mui/material";
import VoucherForm from "../VoucherForm/VoucherForm";
import { UserContext } from "../config/Context";

const PaymentForm = forwardRef(function PaymentForm(
  { onCancel, urs, me, pis, setIsCanceled, showSnackbar, data, open },
  ref
) {
  const dialog2 = useRef();

  const voucherFormRef = useRef();

  const [loading, setLoading] = useState();

  const [voucher, setVoucher] = useState(null);
  const [code, setCode] = useState("");

  const [finalPrice, setFinalPrice] = useState(100000);
  const [totalPisPrice, setTotalPisPrice] = useState(0);

  const { currentUser } = useContext(UserContext);

  const [open2, setOpen2] = useState(false);
  const [data2, setData2] = useState({
    message: "Nạp dữ liệu thành công",
    severity: "success",
  });

  const showSnackbar2 = (message, severity) => {
    setData2({
      message: message,
      severity: severity,
    });

    setOpen2(true);

    setTimeout(() => {
      setOpen2(false);
    }, 5000);
  };

  useImperativeHandle(ref, () => {
    return {
      open() {
        dialog2.current.style.border = "none";
        // dialog2.current.style.overflow = "hidden";
        dialog2.current.style.width = "50%";
        dialog2.current.style.height = "100%";
        dialog2.current.showModal();
      },

      close() {
        dialog2.current.close();
      },
    };
  });

  const handleMOMOPayment = async (amount, mrlId) => {
    setLoading(true);
    let response;
    try {
      response = await authAPI().post(
        endpoints["benhnhanMOMOPayment"],
        {
          amount,
          mrlId,
          voucherId: voucher ? voucher.id : null,
          meId: me ? me.id : null,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );

      if (response.status === 200) {
        showSnackbar2("Đang chuyển hướng thanh toán MOMO ... ", "warning");
        setTimeout(() => {
          window.location.href = response.data;
        }, 3000);
        setTimeout(() => {
          setLoading(false);
        }, 5000);
      } else {
        showSnackbar2(response.data, "error");
      }
    } catch {
      showSnackbar2("Lỗi", "error");
      console.log(response);
    }
  };

  const handleVNPAYPayment = async (amount, mrlId) => {
    setLoading(true);
    let response;
    try {
      response = await authAPI().post(
        endpoints["benhnhanVNPAYPayment"],
        {
          amount,
          mrlId,
          voucherId: voucher ? voucher.id : null,
          meId: me ? me.id : null,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );

      if (response.status === 200) {
        showSnackbar2(
          "Đang chuyển hướng thanh toán sang trang VNPAY ... ",
          "warning"
        );
        setTimeout(() => {
          window.location.href = response.data;
        }, 3000);
        setTimeout(() => {
          setLoading(false);
        }, 5000);
      } else {
        showSnackbar2(response.data, "error");
      }
    } catch {
      showSnackbar2("Lỗi", "error");
      console.log(response);
    }
  };

  const handleCashPayment = async (amount, mrlId) => {
    setLoading(true);
    let response;
    try {
      response = await authAPI().post(
        endpoints["cashPaymentMrl"],
        {
          amount,
          mrlId,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );

      if (response.status === 200) {
        showSnackbar("Thanh toán tiền mặt thành công", "success");
        onCancel();
        setIsCanceled(true);
        setLoading(false);
      } else {
        showSnackbar(response.data, "error");
        setLoading(false);
      }
    } catch {
      showSnackbar("Lỗi", "error");
      console.log(response);
      setLoading(false);
    }
    setLoading(false);
  };

  function handleOpenVoucherForm() {
    voucherFormRef.current.open();
  }

  function handleCloseVoucherForm() {
    voucherFormRef.current.close();
  }

  const handleApplyVoucher = async () => {
    let response;
    try {
      response = await authAPI().post(
        endpoints["applyVoucherPayment"],
        {
          code: code,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );
      if (response.status === 200) {
        voucherFormRef.current.close();
        setVoucher(response.data); // mới set sẽ vô hàng đợi , nên nếu cập nhật giá ở đây là không thể
        showSnackbar2("Sử dụng mã giảm giá thành công !", "success");
      } else {
        showSnackbar2(response.data, "error");
      }
    } catch {
      showSnackbar2("Lỗi", "error");
      console.log(response);
      console.log(code);
    }
  };

  useEffect(() => {
    const applyDiscount = (price) => {
      const percentSale = voucher?.voucherCondition?.percentSale ?? 0;

      if (typeof price !== "number" || isNaN(price)) {
        throw new Error("Invalid price");
      }
      return price - (price * percentSale) / 100;
    };

    if (me === null && urs !== null && pis === null) {
      setFinalPrice(applyDiscount(100000));
    } else if (me !== null && pis !== null) {
      let sum = handleTotalPisPrice(pis);
      setTotalPisPrice(sum);
      setFinalPrice(applyDiscount(sum));
    }
  }, [voucher, finalPrice, me, pis]);

  function handleTotalPisPrice(pis) {
    return pis.reduce((total, p) => {
      return total + p.medicine.price * p.prognosis;
    }, 0);
  }

  const discountAmount = (price, percent) => (price * percent) / 100;

  const discountDisplay = voucher !== null && (
    <td className="text-center">
      -
      {discountAmount(
        me === null && pis === null ? 100000 : totalPisPrice,
        voucher.voucherCondition.percentSale
      ).toLocaleString("vi-VN")}
      ({voucher.voucherCondition.percentSale}%)
    </td>
  );

  return (
    <>
      <VoucherForm
        ref={voucherFormRef}
        onCancel={handleCloseVoucherForm}
        handleApplyVoucher={handleApplyVoucher}
        code={code}
        setCode={setCode}
        voucher={voucher}
        setVoucher={setVoucher}
        open={open2}
        data={data2}
      />
      <dialog ref={dialog2}>
        {open !== null && data !== null && (
          <CustomerSnackbar
            open={open}
            message={data?.message}
            severity={data?.severity}
          />
        )}
        {open2 !== null && data2 !== null && (
          <CustomerSnackbar
            open={open2}
            message={data2?.message}
            severity={data2?.severity}
          />
        )}
        <div className="payment-phase1-container">
          <button
            className="btn-close position-absolute top-0 end-0 m-3"
            aria-label="Close"
            onClick={() => onCancel()}
          ></button>
          <div className="row">
            <div className="col-12" style={{ fontSize: "medium" }}>
              <div className="row d-flex">
                <div className="col-xs-6 col-sm-6 col-md-6">
                  <address>
                    <strong>Bệnh viện tư nhân HealthCare</strong>
                    <br />
                    Phước Kiển , Nhà Bè
                    <br />
                    Thành phố Hồ Chí Minh
                    <br />
                    <abbr title="Phone">Số điện thoại:</abbr> (+84)293819230
                  </address>
                </div>
                <div className="col-xs-6 col-sm-6 col-md-6 text-end">
                  <p>
                    <em>Mã hóa đơn: </em>
                    <strong></strong>
                  </p>
                  <p>
                    <em>Ngày lập hóa đơn: </em>
                    <strong>{dayjs(new Date()).format("DD/MM/YYYY")}</strong>
                  </p>
                </div>
              </div>
              <div className="row">
                <div className="text-center">
                  <h1 style={{ color: "green", marginTop: "10px" }}>
                    {me === null
                      ? "HÓA ĐƠN ĐĂNG KÍ KHÁM BỆNH"
                      : "HÓA ĐƠN THANH TOÁN ĐƠN THUỐC"}
                  </h1>
                </div>

                <div className="container mt-2">
                  <div className="card">
                    <div className="card-body">
                      <h4 className="card-title">Thông tin bệnh nhân : </h4>

                      {me === null && urs !== null && (
                        <>
                          <div className="row d-flex">
                            <div className="col-xs-6 col-sm-6 col-md-6">
                              <p>
                                <span>Mã phiếu đăng ký : </span>
                                <strong className="d-inline">
                                  #MSPDKLK{urs.id}
                                </strong>
                              </p>
                              <p>
                                <span>Tên người đăng ký : </span>
                                <strong>{urs.name}</strong>
                              </p>
                              <p>
                                <span>Số điện thoại : </span>
                                <strong>{urs.user?.phone}</strong>
                              </p>
                            </div>
                            <div className="col-xs-6 col-sm-6 col-md-6 text-end">
                              <p>
                                <span>Ngày đăng ký khám : </span>
                                <strong>
                                  {dayjs(urs.createdDate).format("DD/MM/YYYY")}
                                </strong>
                              </p>
                              <p>
                                <span>Ngày hẹn khám : </span>
                                <strong>
                                  {dayjs(urs.schedule?.date).format(
                                    "DD/MM/YYYY"
                                  )}
                                </strong>
                              </p>
                            </div>
                          </div>
                          <div className="row">
                            <div className="col-xs-12">
                              <p>
                                <span>
                                  Nhu cầu khám :{" "}
                                  <strong className="d-inline">
                                    {urs.favor}
                                  </strong>
                                </span>
                              </p>
                            </div>
                          </div>
                        </>
                      )}
                      {me !== null && urs !== null && (
                        <>
                          <div className="row d-flex">
                            <div className="col-xs-6 col-sm-6 col-md-6">
                              <p>
                                <span>Mã phiếu khám : </span>
                                <strong className="d-inline">
                                  #MSPK{me.id}
                                </strong>
                              </p>
                              <p>
                                <span>Tên bệnh nhân : </span>
                                <strong>{urs.name}</strong>
                              </p>
                              <p>
                                <span>Số điện thoại : </span>
                                <strong>{urs.user.phone}</strong>
                              </p>
                            </div>
                            <div className="col-xs-6 col-sm-6 col-md-6 text-end">
                              <p>
                                <span>Ngày lập phiếu khám : </span>
                                <strong>
                                  {dayjs(me.createdDate).format("DD/MM/YYYY")}
                                </strong>
                              </p>

                              <p>
                                <span>Bác sĩ : </span>
                                <strong>
                                  {me.userCreated.name} ({me.userCreated.phone})
                                </strong>
                              </p>
                              <p>
                                <span>Ngày tái khám : </span>
                                <strong>
                                  {me.followUpDate === null
                                    ? "Không có"
                                    : dayjs(me.followUpDate).format(
                                        "DD/MM/YYYY"
                                      )}
                                </strong>
                              </p>
                            </div>
                          </div>
                          <div className="row">
                            <div className="col-xs-12">
                              <p>
                                <span>
                                  Số ngày cấp thuốc :{" "}
                                  <strong className="d-inline">
                                    {me.durationDay}
                                  </strong>
                                </span>
                              </p>
                              <p>
                                <span>
                                  Chuẩn đoán :{" "}
                                  <strong className="d-inline">
                                    {me.predict}
                                  </strong>
                                </span>
                              </p>
                              <p>
                                <span>
                                  Lời khuyên :{" "}
                                  <strong className="d-inline">
                                    {me.advance}
                                  </strong>
                                </span>
                              </p>
                            </div>
                          </div>
                        </>
                      )}
                    </div>
                  </div>
                </div>
                <div className="container">
                  {me !== null && urs !== null && (
                    <table className="table table-hover">
                      <thead>
                        <tr>
                          <th>Danh sách thuốc :</th>
                          <th className="text-center">Giá tiền</th>
                          <th className="text-center">Số lượng cấp</th>
                          <th className="text-center">Đơn vị thuốc</th>
                          <th className="text-center">Tổng tiền</th>
                        </tr>
                      </thead>
                      <tbody>
                        {pis !== null &&
                          pis.length > 0 &&
                          pis.map((p) => {
                            return (
                              <tr>
                                <td class="col-md-9">
                                  <h4>
                                    <em>{p.medicine.name}</em>
                                  </h4>
                                  {p.usage}
                                </td>
                                <td class="col-md-1 text-center">
                                  {p.medicine.price.toLocaleString("vi-VN")}
                                </td>
                                <td class="col-md-1 text-center">
                                  {p.prognosis}
                                </td>
                                <td class="col-md-1 text-center">
                                  {p.medicine.unitType.unitName}
                                </td>
                                <td class="col-md-1 text-center">
                                  {(
                                    p.medicine.price * p.prognosis
                                  ).toLocaleString("vi-VN")}
                                </td>
                              </tr>
                            );
                          })}
                      </tbody>
                    </table>
                  )}
                  {me === null && urs !== null && (
                    <table className="table table-hover">
                      <thead>
                        <tr>
                          <th>Tiền khám </th>
                          <th className="text-center"></th>
                          <th className="text-center"></th>
                          <th className="text-center"></th>
                          <th className="text-center">Tổng tiền</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td className="col-md-9">
                            <h5>
                              <em>Tiền khám cơ bản </em>
                            </h5>
                          </td>
                          <td></td>
                          <td></td>
                          <td> </td>
                          <td className="text-center">100.000</td>
                        </tr>
                      </tbody>
                    </table>
                  )}
                  <table className="table table-hover">
                    <tbody>
                      <tr>
                        <td className="col-md-9">
                          {voucher !== null && (
                            <h5>
                              <em className="text text-success">
                                Áp dụng mã giảm giá{" "}
                              </em>
                            </h5>
                          )}
                        </td>
                        <td></td>
                        <td></td>
                        <td> </td>
                        {discountDisplay}
                      </tr>
                    </tbody>
                  </table>
                  {isBENHNHAN(currentUser) && (
                    <div className="w-100 d-flex justify-content-between border shadow-sm align-item-center p-3">
                      <div className="bg-success p-1 rounded">
                        <i className="fa-solid fa-ticket fs-5 mr-5"></i>{" "}
                        <span className="text text-white bg-success fs-5 p-1">
                          HEALTHCARE VOUCHER
                        </span>
                      </div>
                      <div className="text text-primary">
                        <button
                          className="btn btn-primary"
                          onClick={handleOpenVoucherForm}
                        >
                          Nhập mã
                        </button>{" "}
                      </div>
                    </div>
                  )}
                  <table className="table table-hover">
                    <thead>
                      <tr>
                        <th>Số tiền cần thanh toán :</th>
                        <th className="text-center"></th>
                        <th className="text-center"></th>
                        <th className="text-center"></th>
                        <th className="text-center">Tổng tiền</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td className="col-md-9">
                          <h4>
                            <em>Tổng cộng </em>
                          </h4>
                        </td>
                        <td>  </td>
                        <td>  </td>
                        <td>  </td>

                        <td className="text-center text-danger">
                          <h4>
                            <strong>
                              {finalPrice.toLocaleString("vi-VN")}
                            </strong>
                          </h4>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>

                {urs !== null &&
                  (loading ? (
                    <div className="d-flex justify-content-center align-item-center">
                      <CircularProgress className="mt-3" />
                    </div>
                  ) : (
                    currentUser !== null && (
                      <>
                        {isBENHNHAN(currentUser) && (
                          <div className="container">
                            <div className="d-flex justify-content-center">
                              <div className="p-3 w-50">
                                <button
                                  className="button-vnpay-payment"
                                  onClick={() =>
                                    handleVNPAYPayment(finalPrice, urs.id)
                                  }
                                >
                                  <div className="icon-vnpay-div">
                                    <img
                                      className="icon-vnpay-image"
                                      src="https://res.cloudinary.com/diwxda8bi/image/upload/v1725081067/vnpay_g4m1a7.png"
                                    />
                                  </div>
                                  <div className="text-payment">
                                    VNPAY Payment
                                  </div>
                                </button>
                              </div>

                              <div className="p-3 w-50">
                                <button
                                  className="button-momo-payment"
                                  onClick={() =>
                                    handleMOMOPayment(finalPrice, urs.id)
                                  }
                                >
                                  <div className="icon-momo-div">
                                    <img
                                      className="icon-momo-image"
                                      src="https://res.cloudinary.com/diwxda8bi/image/upload/v1725079829/momo_payment_i0xokf.png"
                                    />
                                  </div>
                                  <div className="text-payment">
                                    MOMO Payment
                                  </div>
                                </button>
                              </div>
                            </div>
                          </div>
                        )}

                        {isYTA(currentUser) && (
                          <div className="d-flex justify-content-center">
                            <div className="p-3 w-100 d-flex justify-content-center">
                              <button
                                className="btn btn-primary w-50"
                                onClick={() =>
                                  handleCashPayment(finalPrice, urs.id)
                                }
                              >
                                <div className="text-center text-lg">
                                  Thanh toán tiền mặt
                                </div>
                              </button>
                            </div>
                          </div>
                        )}
                      </>
                    )
                  ))}
              </div>
            </div>
          </div>
        </div>
      </dialog>
    </>
  );
});

export default PaymentForm;
