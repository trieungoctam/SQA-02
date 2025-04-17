import {
  forwardRef,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from "react";
import "./LineProcessing.css";
import { authAPI, endpoints } from "../config/Api";
import dayjs from "dayjs";

const LineProcessing = forwardRef(function LineProcessing(
  { onClose, urs, setUrs, voucherGift, setVoucherGift },
  ref
) {
  const dialog = useRef();
  const [imageValue, setImageValue] = useState(-1);
  const [status, setStatus] = useState("");

  useImperativeHandle(ref, () => {
    return {
      open() {
        dialog.current.style.border = "none";
        dialog.current.style.background = "white";
        dialog.current.style.width = "80%";
        dialog.current.style.height = "90%";
        dialog.current.style.overflowY = "hidden";
        dialog.current.showModal();
      },

      close() {
        dialog.current.close();
      },
    };
  });

  const goodOrder = [
    "CHECKING",
    "PAYMENTPHASE1",
    "SUCCESS",
    "PROCESSING",
    "PAYMENTPHASE2",
    "FINISHED_FOLLOWUP",
  ];

  const badOrder = ["FAILED", "CANCELED"];

  const goodeOrderImage = {
    0: {
      status: "CHECKING",
      image:
        "https://res.cloudinary.com/diwxda8bi/image/upload/v1727156436/iStock-1095111248-750x422-1_ths1p1.jpg",
    },
    1: {
      status: "PAYMENTPHASE1",
      image:
        "https://res.cloudinary.com/diwxda8bi/image/upload/v1727164769/Untitled3_fxanjf.png",
    },
    2: {
      status: "SUCCESS",
      image:
        "https://res.cloudinary.com/diwxda8bi/image/upload/v1727165863/Telpo-TPS980-QR-2_qchitw.jpg",
    },
    3: {
      status: "PROCESSING",
      image:
        "https://res.cloudinary.com/diwxda8bi/image/upload/v1727166874/doctor_ao5tyq.png",
    },
    4: {
      status: "PAYMENTPHASE2",
      image:
        "https://res.cloudinary.com/diwxda8bi/image/upload/v1727167744/p2_nm8eol.png",
    },
    5: {
      status: "FINISHED_FOLLOWUP",
      image:
        "https://res.cloudinary.com/diwxda8bi/image/upload/v1727168407/medication_jvtvn0.jpg",
    },
    6: {
      CANCELED: {
        image:
          "https://res.cloudinary.com/diwxda8bi/image/upload/v1727162068/istockphoto-1189267550-612x612_uquosq.jpg",
      },
      FAILED: {
        image:
          "https://res.cloudinary.com/diwxda8bi/image/upload/v1727161768/bigstock-Failure-stamp-48396098-760x586_mvdigl.jpg",
      },
      ANY: {
        image:
          "https://res.cloudinary.com/diwxda8bi/image/upload/v1727159892/Success-key_w2nwvg.jpg",
      },
    },
  };

  useEffect(() => {
    if (urs !== null) {
      const s = urs?.statusIsApproved?.status;
      let statusIndex = goodOrder.findIndex((o) => s === o);
      setStatus(s);
      setImageValue(statusIndex >= 0 ? statusIndex : 6);
      clearHTMLDOM();
      addHTMLDOM(s, statusIndex);
      if (urs.isVoucherTaken === true) {
        receiveVoucherGift(urs?.id);
      }
    }
  }, [urs]);

  useEffect(() => {}, [voucherGift]);

  function clearHTMLDOM() {
    let clearClassName = document.querySelectorAll(".finished");
    clearClassName.forEach((e) => {
      if (e.getAttribute("name") !== "DEFAULT") {
        e.classList.remove("finished");
      }
    });

    clearClassName = document.querySelectorAll(".doing");
    clearClassName.forEach((e) => {
      if (e.getAttribute("name") !== "DEFAULT") {
        e.classList.remove("doing");
      }
    });

    clearClassName = document.querySelectorAll(".fa-beat");
    clearClassName.forEach((e) => {
      if (e.getAttribute("name") !== "DEFAULT") {
        e.classList.remove("fa-beat");
      }
    });

    clearClassName = document.querySelectorAll(".failed");
    clearClassName.forEach((e) => {
      if (e.getAttribute("name") !== "DEFAULT") {
        e.classList.remove("failed");
      }
    });
  }

  function addHTMLDOM(status, statusIndex) {
    if (statusIndex >= 0)
      for (let i = 0; i <= statusIndex; i++) {
        let elementId = document.getElementById(goodOrder[i]);
        let elementName = document.getElementsByName(goodOrder[i]);

        if (elementName.length > 0 && statusIndex !== 0 && i !== statusIndex)
          elementName.forEach((e) => {
            e.classList.add("finished");
          });

        if (i !== statusIndex) {
          elementId.classList.add("finished");
        } else if (i === statusIndex) {
          elementId.classList.add("doing", "fa-beat");
        }
      }
    else {
      if (status === "CANCELED" || status === "FAILED") {
        let elementId = document.getElementById("CHECKING");
        let elementName = document.getElementsByName("CHECKING");
        elementId.classList.add("failed");
        elementName.forEach((e) => {
          if (elementName.length > 0 && statusIndex !== 0)
            e.classList.add("failed");
        });
      } else if (status === "FOLLOWUP" || status === "FINISHED") {
        for (let i = 0; i <= 5; i++) {
          let elementId = document.getElementById(goodOrder[i]);
          let elementName = document.getElementsByName(goodOrder[i]);

          if (elementName.length > 0 && statusIndex !== 0)
            elementName.forEach((e) => {
              e.classList.add("finished");
            });

          if (i !== statusIndex) {
            elementId.classList.add("finished");
          }
        }
      }
    }
  }

  function handleImageValue(imageValue) {
    setImageValue(imageValue);
  }

  function handleRecieveVoucher() {
    let elementGift = document.getElementById("gift");
    elementGift.classList.add("fa-shake");
    setTimeout(() => {
      receiveVoucherGift(urs.id);
    }, 1000);
  }

  /*
    Giải thích problem tại sao thay đổi state voucherGift , nạp lại cả cha UserRegisterScheduleList component mà ko đc ?
    Nguyên nhân : urs là state đc lưu khi BẤM vào id của phiếu đăng ký, 
                  vì thế cập nhật toàn bộ userRegisterScheduleList là vô nghĩa.
    Cách giải quyết : chỉnh trực tiếp trường isVoucherTaken của object urs đang lưu thành true,
                      đưa vào useEffect, nạp lại mỗi component LineProcessing này.
                      Đẩy voucherGift State lên component cha cho dễ quản lý, chứ để đây ko ảnh hưởng gì
  */

  const receiveVoucherGift = async (mrlId) => {
    let response;
    try {
      if (mrlId !== null && mrlId !== undefined) {
        response = await authAPI().get(endpoints["receiveVoucherGift"](mrlId), {
          validateStatus: function (status) {
            return status < 500;
          },
        });
        if (response.status === 200) {
          setVoucherGift(response.data);
          setUrs((prev) => ({
            ...prev,
            isVoucherTaken: true,
          }));
        }
      }
    } catch {}
  };

  return (
    <>
      <dialog className="container" ref={dialog}>
        <div onClick={onClose} className="close-button">
          X
        </div>
        <div className="line-processing-container fs-3 text">
          <h1 className="text-center text text-primary">QUÁ TRÌNH HIỆN TẠI</h1>
          <div className="process-step">
            <div
              id="DEFAULT"
              name="DEFAULT"
              role="button"
              className="step-circle finished"
            >
              <i class="fa-solid fa-registered"></i>
            </div>
            <div name="DEFAULT" className="step-line finished"></div>

            <div
              id="CHECKING"
              name="CHECKING"
              role="button"
              className="step-circle"
              onClick={(e) => handleImageValue(0)}
            >
              <i class="fa-solid fa-hourglass-start"></i>
            </div>
            <div name="CHECKING" className="step-line"></div>

            <div
              name="CHECKING"
              role="button"
              className="step-circle"
              onClick={(e) => handleImageValue(6)}
            >
              <i class="fa-solid fa-clipboard-check"></i>
            </div>
            <div name="CHECKING" className="step-line"></div>

            <div
              id="PAYMENTPHASE1"
              name="PAYMENTPHASE1"
              role="button"
              className="step-circle"
              onClick={(e) => handleImageValue(1)}
            >
              <i class="fa-solid fa-credit-card"></i>
            </div>
            <div name="PAYMENTPHASE1" className="step-line"></div>

            <div
              id="SUCCESS"
              name="SUCCESS"
              role="button"
              className="step-circle"
              onClick={(e) => handleImageValue(2)}
            >
              <i class="fa-solid fa-qrcode"></i>
            </div>
            <div name="SUCCESS" className="step-line"></div>

            <div
              id="PROCESSING"
              name="PROCESSING"
              role="button"
              className="step-circle"
              onClick={(e) => handleImageValue(3)}
            >
              <i class="fa-solid fa-user-doctor"></i>
            </div>
            <div name="PROCESSING" className="step-line"></div>

            <div
              id="PAYMENTPHASE2"
              name="PAYMENTPHASE2"
              role="button"
              className="step-circle"
              onClick={(e) => handleImageValue(4)}
            >
              <i class="fa-solid fa-credit-card"></i>
            </div>
            <div name="PAYMENTPHASE2" className="step-line"></div>

            <div
              id="FINISHED_FOLLOWUP"
              name="FINISHED_FOLLOWUP"
              role="button"
              className="step-circle"
              onClick={(e) => handleImageValue(5)}
            >
              <i class="fa-solid fa-circle-check"></i>
            </div>

            {(status === "FINISHED" || status === "FOLLOWUP") && (
              <>
                <div
                  name="FINISHED_FOLLOWUP"
                  className="step-line finished"
                ></div>

                <div
                  name="FINISHED_FOLLOWUP"
                  role="button"
                  className="step-circle finished fa-beat"
                  onClick={(e) => handleImageValue(7)}
                >
                  <i class="fa-solid fa-ticket"></i>
                </div>
              </>
            )}
          </div>
          <div className="process-image">
            {status !== "FINISHED" &&
            status !== "FOLLOWUP" &&
            imageValue <= 6 ? (
              <img
                src={
                  imageValue < 6
                    ? goodeOrderImage[imageValue]?.image
                    : goodeOrderImage[imageValue]?.[status] === undefined
                    ? goodeOrderImage[imageValue]?.ANY.image
                    : goodeOrderImage[imageValue]?.[status].image
                }
              />
            ) : (
              <>
                <div className="thanks-container">
                  <h2 className="text text-warning fw-bold">QUÀ CẢM ƠN</h2>
                  <p className="text text-center text-success">
                    Cảm ơn quý khách đã đồng hành với chúng tôi trong suốt hành
                    trình khám chữa bệnh !
                  </p>
                  <p>
                    Chúng tôi xin tặng một phiếu giảm giá khi thanh toán dịch vụ
                    của chúng tôi :
                  </p>

                  {urs?.isVoucherTaken !== false ? (
                    <div className="thanks-voucher-container">
                      <div className="voucher-info">
                        <p>
                          Mã giảm giá :{" "}
                          <span className="fw-bold">{voucherGift?.code}</span>
                        </p>
                        <p>
                          Ngày hết hạn :{" "}
                          <span className="fw-bold">
                            {dayjs(
                              voucherGift?.voucherCondition?.expiredDate
                            ).format("DD/MM/YYYY")}
                          </span>
                        </p>
                      </div>
                      <p className="text-center">
                        Chi tiết :{" "}
                        <span className="fw-bold">
                          {voucherGift?.description}
                        </span>
                      </p>
                      <p className="fst-italic text-center">
                        * Mã giảm giá này chỉ được sử dụng 1 lần và có hạn là 3
                        tháng kể từ ngày nhận
                      </p>
                    </div>
                  ) : (
                    <>
                      <div
                        className="gift-container mt-5"
                        onClick={handleRecieveVoucher}
                      >
                        <i
                          id="gift"
                          role="button"
                          class="fa-solid fa-gift fa-bounce"
                          style={{ color: "#FFD43B" }}
                        ></i>
                      </div>
                      <p className="mt-5">Nhấn vào quà tặng để nhận</p>
                    </>
                  )}
                </div>
              </>
            )}
          </div>
        </div>
      </dialog>
    </>
  );
});

export default LineProcessing;
