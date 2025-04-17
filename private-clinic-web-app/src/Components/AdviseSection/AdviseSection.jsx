import { useCallback, useContext, useEffect, useRef, useState } from "react";
import "./AdviseSection.css";
import Api, { authAPI, endpoints } from "../config/Api";
import { Alert, CircularProgress, Pagination } from "@mui/material";
import { CustomerSnackbar, isBENHNHAN } from "../Common/Common";
import { UserContext } from "../config/Context";
import { useNavigate, useSearchParams } from "react-router-dom";
import dayjs from "dayjs";
import CountLikeBlog from "../CountLikeBlog/CountLikeBlog";

export default function AdviseSection() {
  const [blogs, setBlogs] = useState([]);

  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);

  const [blogId, setBlogId] = useState(0);

  const [likeChange, setLikeChange] = useState(false);

  const [createBlogForm, setCreateBlogForm] = useState({
    title: "",
    content: "",
  });

  const [filterParams, setFilterParams] = useState({
    key: "",
  });

  const [params] = useSearchParams();
  const navigate = useNavigate();

  const [comment, setComment] = useState(null);

  const [answerBlogContent, setAnswerBlogContent] = useState("");

  const { currentUser } = useContext(UserContext);

  const [loading, setLoading] = useState();
  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Đặt câu hỏi thành công",
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
    }, 2400);
  };

  const getAllBlogs = useCallback(async () => {
    let response;

    try {
      let url = `${endpoints["getAllBlogs"]}?page=${page}`;

      let key = params.get("key");
      if (key) url += `&key=${key}`;

      const typeFeitch = currentUser ? authAPI() : Api;

      response = await typeFeitch.get(url, {
        validateStatus: function (status) {
          return status < 500;
        },
      });

      if (response.status === 200) {
        setTotalPage(response.data.totalPages);
        setBlogs(response.data);
      } else console.log(response.data);
    } catch {
      console.log(response.data);
    }
  }, [page, params, currentUser, likeChange]);

  useEffect(() => {
    getAllBlogs();
  }, [page, blogId, createBlogForm, params, comment, currentUser, likeChange]);

  function handleToggleShowAnswer(blogId, blogIdState) {
    if (comment === null || blogId !== blogIdState) {
      setBlogId(blogId);
      handleGetCommentBlogByBlogId(blogId);
    }
  }

  function handleCreateCommentBlog(blogId) {
    setBlogId(blogId);
  }

  function handleAutoFillCreateBlogFormAtt(e) {
    const { name, value } = e.target;
    setCreateBlogForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  function handleAutoFillFilterFormAtt(e) {
    const { name, value } = e.target;
    setFilterParams((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  const handleCreateBlogForm = async (event) => {
    event.preventDefault();
    setLoading(true);
    if (currentUser !== null) {
      let response;
      try {
        response = await authAPI().post(
          endpoints["createNewBlog"],
          {
            ...createBlogForm,
          },
          {
            validateStatus: function (status) {
              return status < 500;
            },
          }
        );
        if (response.status === 201) {
          showSnackbar("Đặt câu hỏi thành công !", "success");
          setCreateBlogForm({
            title: "",
            content: "",
          });
        } else {
          showSnackbar(response.data, "error");
          setCreateBlogForm({
            title: "",
            content: "",
          });
        }
      } catch {
        showSnackbar(response, "error");
        console.log(response);
        setCreateBlogForm({
          title: "",
          content: "",
        });
        setLoading(false);
      }
    } else {
      showSnackbar("Bạn cần đăng nhập để đặt câu hỏi !", "error");
      setLoading(false);
    }
    setLoading(false);
  };

  const handleCreateNewCommentBlog = async (event, blogId) => {
    event.preventDefault();
    setLoading(true);
    if (currentUser !== null) {
      let response;
      try {
        response = await authAPI().post(
          endpoints["createNewCommentBlog"],
          {
            content: answerBlogContent,
            blogId: blogId,
          },
          {
            validateStatus: function (status) {
              return status < 500;
            },
          }
        );
        if (response.status === 201) {
          showSnackbar("Trả lời câu hỏi thành công !", "success");
          setAnswerBlogContent("");
          handleGetCommentBlogByBlogId(blogId);
        } else {
          showSnackbar(response.data, "error");
        }
      } catch {
        showSnackbar(response, "error");
        console.log(response);
        setLoading(false);
      }
    } else {
      showSnackbar("Bạn cần đăng nhập để trả lời câu hỏi !", "error");
    }
    setLoading(false);
  };

  function handleFilterAdvises(event) {
    event.preventDefault();
    navigate(`?key=${filterParams.key}`);
  }

  const handleGetCommentBlogByBlogId = async (blogId) => {
    let response;
    try {
      response = await Api.get(endpoints["getCommentBlogByBlogId"](blogId), {
        validateStatus: function (status) {
          return status < 500;
        },
      });
      if (response.status === 200 || response.status === 204) {
        setComment(response.data);
      }
    } catch {
      showSnackbar(response, "error");
    }
  };

  function handleLike(blogId) {
    if (currentUser !== null) {
      const likeIcon = document.getElementById(`likeBlog${blogId}`);
      const likeCount = document.getElementById(`countLikeBlog${blogId}`);

      if (likeIcon && likeCount) {
        if (likeIcon.classList.contains("fa-solid")) {
          likeIcon.classList.remove("fa-solid");
          likeCount.textContent = parseInt(likeCount.textContent) - 1;
        } else if (!likeIcon.classList.contains("fa-solid")) {
          likeIcon.classList.add("fa-solid");
          likeCount.textContent = parseInt(likeCount.textContent) + 1;
        }
      }
      setTimeout(() => {
        handleToggleLikeApi(blogId);
      }, 500);
    } else {
      showSnackbar("Bạn cần đăng nhập để thích bài viết !", "error");
    }
  }

  const handleToggleLikeApi = async (blogId) => {
    let response;
    try {
      response = await authAPI().post(endpoints["toggleLikeBlog"](blogId), {
        validateStatus: function (status) {
          return status < 500;
        },
      });
      if (response.status === 200) {
        setLikeChange((prev) => !prev);
      } else showSnackbar(response, "error");
    } catch {
      showSnackbar(response, "error");
    }
  };

  return (
    <>
      <div className="advise-container">
        <div className="advise-container-search">
          <div className="container-fluid text-center p-4">
            <h2 className="section-title">CHUYÊN MỤC TƯ VẤN</h2>
            <div className="underline mx-auto"></div>
          </div>

          <div className="container-fluid bg-primary search-section py-5">
            <div className="text-center text-white">
              <h5>Chúng tôi có thể giúp gì cho bạn</h5>
            </div>
            <form onSubmit={handleFilterAdvises}>
              <div className="d-flex justify-content-center mt-4">
                <div className="search-box">
                  <input
                    id="key"
                    type="text"
                    className="form-control search-input"
                    placeholder="Nhập từ khoá để tìm kiếm"
                    name="key"
                    defaultValue={filterParams.key}
                    onChange={handleAutoFillFilterFormAtt}
                  />
                  <button className="btn search-btn" type="submit">
                    <i className="bi bi-search"></i>
                  </button>
                </div>
              </div>{" "}
            </form>
          </div>
        </div>
        <div className="advise-container-blogs">
          {blogs.empty === true ? (
            <>
              <Alert
                variant="filled"
                severity="warning"
                className="w-50 mx-auto"
              >
                Hiện không câu hỏi nào !
              </Alert>
            </>
          ) : (
            <>
              {blogs.empty === false &&
                blogs.content.map((b) => {
                  return (
                    <>
                      <div
                        key={b.id}
                        className="container my-4 position-relative"
                      >
                        <div className="card p-3 shadow-sm question-card">
                          <div className="card-body">
                            <div className="header-card-container mb-4">
                              <div className="base-infor">
                                <img className="avatar" src={b.user.avatar} />
                                <div className="mb-3">
                                  <i className="bi bi-chat-dots"></i>
                                  <span className="answer-name fw-bold">
                                    {b.user.name}
                                  </span>
                                  <p className="ml-3">
                                    {dayjs(b.createdDate).format("DD/MM/YYYY")}
                                  </p>
                                </div>
                              </div>
                              <div className="like-container">
                                <i
                                  id={`likeBlog${b.id}`}
                                  type="button"
                                  className={`fa-regular fa-heart fa-2xl ${
                                    b.hasLiked === true ? "fa-solid" : ""
                                  }`}
                                  style={{ color: "#ff0019" }}
                                  onClick={() => handleLike(b.id)}
                                ></i>
                                <CountLikeBlog b={b} />
                              </div>
                            </div>

                            <h5 className="card-title fw-bold">{b.title}</h5>
                            <p className="card-text">{b.content}</p>
                            <div className="answerBtn">
                              {b.isCommented === true ? (
                                <button
                                  className="btn btn-primary"
                                  onClick={() =>
                                    handleToggleShowAnswer(b.id, blogId)
                                  }
                                >
                                  Xem câu trả lời
                                </button>
                              ) : currentUser !== null &&
                                !isBENHNHAN(currentUser) ? (
                                <>
                                  <button
                                    className="btn btn-danger"
                                    onClick={() =>
                                      handleCreateCommentBlog(b.id)
                                    }
                                  >
                                    Đưa ra lời khuyên
                                  </button>
                                </>
                              ) : (
                                <>
                                  <button className="btn btn-secondary">
                                    Chưa có câu trả lời
                                  </button>
                                </>
                              )}
                            </div>
                          </div>
                        </div>
                      </div>
                      <div
                        id={`answerBlogId${b.id}`}
                        className="advise-container-item container mb-4 position-relative"
                      >
                        {blogId === b.id &&
                          (b.isCommented === true && b.isCommented !== null ? (
                            <div className="card p-3 shadow-sm d-flex flex-row answer-card">
                              {comment !== null && comment.id > 0 ? (
                                <>
                                  <div className="doctor-avatar me-3">
                                    <img
                                      src={comment.user.avatar}
                                      alt="Bác sĩ"
                                      className="rounded-circle"
                                    />
                                  </div>
                                  <div>
                                    <h6 className="fw-bold">
                                      {comment.user.name}
                                    </h6>
                                    <p className="text-muted mb-1">
                                      {dayjs(comment.createdDate).format(
                                        "DD/MM/YYYY"
                                      )}
                                    </p>
                                    <p className="text-muted mb-2">
                                      Bệnh viện Đa khoa HealthCare TP.HCM
                                    </p>
                                    <p>{comment.content}</p>
                                    {/* <label className="text-decoration-none">
                                      Xem thêm
                                    </label> */}
                                  </div>
                                </>
                              ) : (
                                b.isCommented === false &&
                                b.isCommented !== null && (
                                  <>
                                    <Alert
                                      variant="filled"
                                      severity="warning"
                                      className="w-50 mx-auto"
                                    >
                                      Chưa có câu trả lời nào !
                                    </Alert>
                                  </>
                                )
                              )}
                            </div>
                          ) : (
                            <>
                              {loading ? (
                                <>
                                  <div className="d-flex justify-content-center align-item-center">
                                    <CircularProgress className="mt-3" />
                                  </div>
                                </>
                              ) : (
                                <form
                                  onSubmit={(e) =>
                                    handleCreateNewCommentBlog(e, blogId)
                                  }
                                >
                                  <div className="create-advice-container container mt-2">
                                    <div className="comment-box">
                                      <img
                                        className="avatar"
                                        src={currentUser?.avatar}
                                      />

                                      <textarea
                                        type="text"
                                        className="comment-input"
                                        placeholder="Viết bình luận..."
                                        onChange={(e) =>
                                          setAnswerBlogContent(e.target.value)
                                        }
                                        defaultValue={answerBlogContent}
                                        required
                                      />
                                      <div className="comment-icons mt-2">
                                        <button
                                          type="submit"
                                          className="btn btn-link p-0 text-light"
                                        >
                                          <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="24"
                                            height="24"
                                            fill="currentColor"
                                            className="bi bi-send"
                                            viewBox="0 0 16 16"
                                          >
                                            <path d="M15.854.146a.5.5 0 0 1 .11.53l-5 14a.5.5 0 0 1-.927.06L8.155 10.18l-6.792 2.264a.5.5 0 0 1-.65-.65l2.263-6.793L.262 1.036A.5.5 0 0 1 .32.109l14-5a.5.5 0 0 1 .534.037zm-13.5 7.982 5.149 1.721L14.561 1.44 2.354 8.128zm4.265 5.69 1.721 5.149L14.56 1.439 2.353 8.127z" />
                                          </svg>
                                        </button>
                                      </div>
                                    </div>
                                  </div>
                                </form>
                              )}
                            </>
                          ))}
                      </div>
                    </>
                  );
                })}
            </>
          )}
          <div className="d-flex justify-content-center">
            {blogs.empty === false && (
              <Pagination
                count={totalPage}
                color="primary"
                className="mt-2 mb-3"
                onChange={(event, value) => setPage(value)}
              />
            )}
          </div>
        </div>
      </div>

      <div className="create-blog-container">
        <CustomerSnackbar
          open={open}
          message={data.message}
          severity={data.severity}
        />

        {currentUser !== null && isBENHNHAN(currentUser) && (
          <div className="appointment-form-container">
            <div className="appointment-form">
              <h2 className="text text-primary">Đặt câu hỏi tư vấn</h2>
              <form id="appointmentForm" onSubmit={handleCreateBlogForm}>
                <div className="form-group">
                  <label htmlFor="name">Tiêu đề câu hỏi</label>
                  <input
                    type="text"
                    id="title"
                    name="title"
                    required
                    value={createBlogForm.title}
                    onChange={handleAutoFillCreateBlogFormAtt}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="content">Nội dung : </label>
                  <textarea
                    id="content"
                    name="content"
                    rows="4"
                    required
                    value={createBlogForm.content}
                    onChange={handleAutoFillCreateBlogFormAtt}
                  ></textarea>
                </div>

                {loading ? (
                  <>
                    <div className="d-flex justify-content-center align-item-center">
                      <CircularProgress className="mt-3" />
                    </div>
                  </>
                ) : (
                  <button type="submit">Gửi câu hỏi</button>
                )}
              </form>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
