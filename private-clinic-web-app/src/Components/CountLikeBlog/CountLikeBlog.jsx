import { useEffect, useState } from "react";
import Api, { endpoints } from "../config/Api";

export default function CountLikeBlog({ b }) {
  const [likeCount, setLikeCount] = useState(0);

  useEffect(() => {
    handleCountLikeBlog(b.id);
  });

  const handleCountLikeBlog = async (blogId) => {
    let response;
    try {
      response = await Api.get(endpoints["countLikeBlog"](blogId), {
        validateStatus: function (status) {
          return status < 500;
        },
      });
      if (response.status === 200) {
        setLikeCount(response.data.count);
      } else console.log(response, "error");
    } catch {
      console.log(response, "error");
    }
  };
  return (
    <>
      <strong id={`countLikeBlog${b.id}`} className="d-block mt-3">
        {likeCount}
      </strong>
    </>
  );
}
