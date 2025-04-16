import "./Footer.css";

export default function Footer() {
  return (
    <>
      <div className="footer-container container my-5">
        <footer className="footer text-white text-center text-lg-start">
          <div className="container p-4">
            <div className="row mt-4">
              <div className="col-lg-4 col-md-6 mb-4 mb-md-0">
                <h5 className="text-uppercase mb-4 pb-1 text-center">Vị trí</h5>

                <ul className="fa-ul text-center">
                  <li className="mb-3">
                    <span className="fa-li">
                      <i className="fas fa-home"></i>
                    </span>
                    <span className="ms-2">Nhà Bè, thành phố Hồ Chí Minh</span>
                  </li>
                  <li className="mb-3">
                    <span className="fa-li">
                      <i className="fas fa-envelope"></i>
                    </span>
                    <span className="ms-2">2151050249manh@ou.edu.vn</span>
                  </li>
                  <li className="mb-3">
                    <span className="fa-li">
                      <i className="fas fa-phone"></i>
                    </span>
                    <span className="ms-2">0123456789</span>
                  </li>
                  <li className="mb-3">
                    <span className="fa-li">
                      <i className="fas fa-print"></i>
                    </span>
                    <span className="ms-2">0123456789</span>
                  </li>
                </ul>
              </div>

              <div className="col-lg-4 col-md-6 mb-4 mb-md-0">
                <h5 className="text-uppercase mb-4 text-center">Giờ mở cửa</h5>

                <table className="table text-center text-white">
                  <tbody className="font-weight-normal">
                    <tr>
                      <td>Thứ 2 - Thứ 7:</td>
                      <td>8h sáng - 9h tối</td>
                    </tr>
                    <tr>
                      <td>Chủ nhật:</td>
                      <td>9h sáng - 10 tối</td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <div className="col-lg-4 col-md-12 mb-4 mb-md-0">
                <h5 className="text-uppercase mb-4 text-center">Về phòng khám</h5>

                <p className="text-center">
                CHI NHÁNH CÔNG TY CỔ PHẦN Y TẾ BỆNH VIỆN ĐA KHOA QUỐC TẾ THÀNH PHỐ HỒ CHÍ MINH
                </p>
              </div>
            </div>
          </div>
        </footer>
      </div>
    </>
  );
}
