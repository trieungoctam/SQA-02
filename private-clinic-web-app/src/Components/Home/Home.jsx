import { useContext, useRef, useState } from "react";
import "./Home.css";
import { useEffect } from "react";
import { UserContext } from "../config/Context";
import { Link, useNavigate } from "react-router-dom";
import { CustomerSnackbar } from "../Common/Common";

export default function Home() {
  const { currentUser, setCurrentUser } = useContext(UserContext);
  const navigate = useNavigate();

  let slideIndexRef = useRef(0);

  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Đăng nhập thành công",
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
    }, 2500);
  };

  useEffect(() => {
    if (document !== undefined) {
      showSlides();
    }
  }, [document]);

  function showSlides() {
    let i;
    const slides = document.getElementsByClassName("slide");

    if (slides !== undefined && slides.length > 0) {
      for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
      }
      slideIndexRef.current++;
      if (slideIndexRef.current > slides.length) {
        slideIndexRef.current = 1;
      }
      slides[slideIndexRef.current - 1].style.display = "block";
      setTimeout(showSlides, 4000);
    }
  }

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <div className="slideshow-container">
        <div className="slide fade">
          <img src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726481266/banner-web-T12.2023-copy_myspbe.jpg" />
        </div>
        <div className="slide fade">
          <img
            src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726481265/banner-web-tomec-copy-2-2-1920x555_tzhjru.jpg"
            alt="Slide 2"
          />
        </div>
        <div className="slide fade">
          <img
            src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726481230/banner-ct-1975-dk_jxphws.jpg"
            alt="Slide 3"
          />
        </div>
      </div>
      <div className="section-wrapper">
        <div className="section-content row">
          <div className="col-inner col col-lg-4">
            <h3 className="heading-col">GÓI KHÁM HEALTHCARE</h3>
            <strong>
              <p>Danh mục khám sức khỏe tổng quát và chuyên sâu</p>
            </strong>
            <div className="section-btn-wrapper">
              <button className="section-button">
                <span className="text-button">CHI TIẾT CÁC GÓI</span>
              </button>
            </div>
          </div>
          <div className="col-inner-middle text-center col col-lg-4">
            <h3 className="heading-col">ĐẶT LỊCH</h3>
            <strong>
              <p>Khám sức khoẻ công nghệ cao</p>
            </strong>
            <div className="section-btn-wrapper">
              <button
                className="section-button middle"
                onClick={() => {
                  currentUser === null
                    ? showSnackbar("Bạn cần đăng nhập để đặt lịch !", "error")
                    : navigate("/register-schedule");
                }}
              >
                <span className="text-button">ĐĂNG KÝ TẠI ĐÂY</span>
              </button>
            </div>
          </div>
          <div className="col-inner col col-lg-4">
            <h3 className="heading-col">TƯ VẤN</h3>
            <strong>
              <p>Tổng hợp các lời khuyên tư vấn bệnh lý từ chuyên gia bác sĩ</p>
            </strong>
            <div className="section-btn-wrapper">
              <button
                className="section-button"
                onClick={() => {
                  navigate("/advise-section");
                }}
              >
                <span className="text-button">XEM NGAY</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="clinic-wrapper text-center">
        <h2 class="title-clinic">PHÒNG KHÁM ĐA KHOA</h2>
        <p>TRỰC THUỘC CTCP BỆNH VIỆN ĐA KHOA QUỐC TẾ</p>
        <div class="container clinic-image-wrapper">
          <div class="row">
            <div class="col col-lg-4">
              <div class="box-image">
                <img
                  src="https://tomec.vn/wp-content/uploads/2023/06/Clip-path-group-3.png"
                  alt="clinic-1"
                  class="clinic-image"
                />
                <h4 class="tittle-img">Không gian y tế đẳng cấp</h4>
              </div>
            </div>
            <div class="col col-lg-4">
              <div class="box-image">
                <img
                  src="https://tomec.vn/wp-content/uploads/2023/06/Layer-5.png"
                  alt="clinic-1"
                  class="clinic-image"
                />
                <h4 class="tittle-img">
                  Hệ thống máy móc, trang thiết bị y tế công nghệ cao
                </h4>
              </div>
            </div>
            <div class="col col-lg-4">
              <div class="box-image">
                <img
                  src="https://tomec.vn/wp-content/uploads/2023/06/Clip-path-group-3.png"
                  alt="clinic-1"
                  class="clinic-image"
                />
                <span class="tittle-img">
                  Đội ngũ Chuyên gia – Bác sĩ giàu kinh nghiệm
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="features-container-main container">
        <h1 className="text text-center text-primary mt-5 mb-5">Đặc điểm nổi trội</h1>
        <div className="features-container">
          <div className="feature-item">
            <img src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726497840/gtr1_qskem9.png" />
            <p className="mt-2">CHUYÊN GIA HÀNG ĐẦU</p>
          </div>
          <div className="feature-item">
            <img src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726497841/gtr4_lx5kmq.png" />
            <p className="mt-2">QUÁ TRÌNH ĐIỀU TRỊ CHUYÊN NGHIỆP</p>
          </div>
          <div className="feature-item">
            <img src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726497841/gtr3_jtddba.png" />
            <p className="mt-2">ĐIỀU TRỊ HIỆU QUẢ</p>
          </div>
          <div className="feature-item">
            <img src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726497841/gtr2_jymfkg.png" />
            <p className="mt-2">THIẾT BỊ HIỆN ĐẠI</p>
          </div>
          <div className="feature-item">
            <img src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726497845/gtr5_q4qvfc.png" />
            <p className="mt-2">CHI PHÍ HỢP LÝ</p>
          </div>
        </div>
      </div>

      <div class="service-wrapper">
        <h2 class="service-tittle text-center mt-5 mb-5">DỊCH VỤ THẾ MẠNH</h2>
        <div class="container">
          <div class="row">
            <div class="col service-col col-lg-3">
              <img
                src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726494687/1f017d385a0eff50a61f_p77sah.jpg"
                alt="Image-1"
              />
              <div class="img-service-desc">
                <span>KHÁM SỨC KHỎE TỔNG QUÁT</span>
              </div>
            </div>
            <div class="col service-col col-lg-3">
              <img
                src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726494687/1f017d385a0eff50a61f_p77sah.jpg"
                alt="Image-2"
              />
              <div class="img-service-desc">
                <span>CHẨN ĐOÁN HÌNH ẢNH</span>
              </div>
            </div>
            <div class="col service-col col-lg-3">
              <img
                src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726494687/1f017d385a0eff50a61f_p77sah.jpg"
                alt="Image-3"
              />
              <div class="img-service-desc">
                <span>NỘI SOI TIÊU HÓA</span>
              </div>
            </div>
            <div class="col service-col col-lg-3">
              <img
                src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726494687/1f017d385a0eff50a61f_p77sah.jpg"
                alt="Image-4"
              />
              <div class="img-service-desc">
                <span>XÉT NGHIỆM</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="contact-container-main">
        <h2 className="text text-primary text-center mt-3 mb-3">
          <strong>LIÊN HỆ TƯ VẤN</strong>
        </h2>
        <div className="contact-container">
          <div className="left-contact">
            <img
              className="image-contact"
              src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726483669/bg-ta1_g90fiw.jpg"
            />
          </div>
          <div className="right-contact">
            <img src="https://res.cloudinary.com/diwxda8bi/image/upload/v1726483669/bg-ta2_w7qx6c.jpg" />
          </div>
          <div className="infor-contact">
            <div className="infor-left-contact">
              <h2 className="text-center">TP.HỒ CHÍ MINH</h2>
              <h4 className="text-center">
                <i class="fa-solid fa-location-dot mr-4"></i>{" "}
                <span>1234 Phước Kiển , Nhà Bè</span>
              </h4>
              <h4 className="text-center">0987654321</h4>
              <h4 className="text-center">0123456789</h4>
            </div>
            <div className="infor-right-contact">
              <h2 className="text-center">NHẮN TIN VỚI CHUYÊN VIÊN TƯ VẤN</h2>
              <div className="d-flex align-item-center justify-content-center">
                <button
                  className="btn btn-secondary"
                  onClick={() => {
                    currentUser === null
                      ? showSnackbar("Bạn cần đăng nhập để nhắn tin !", "error")
                      : navigate("/chatting");
                  }}
                >
                  Nhắn tin
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
