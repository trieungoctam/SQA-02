import React, { useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import rehypeHighlight from "rehype-highlight";
import "./ResultPrompt.css"

function ResultPrompt({ content, loading }) {
  const [copySuccess, setCopySuccess] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(content).then(() => {
      setCopySuccess(true);
      setTimeout(() => setCopySuccess(false), 1500);
    });
  };

  return (
    <div className="seoResult">
      <div className="resultHeader">
        <h2 className="mb-5 text-center">KẾT QUẢ LỜI KHUYÊN TƯ VẤN</h2>
        {content && !copySuccess && (
          <i className="fa-regular fa-copy" onClick={handleCopy}></i>
        )}
        {copySuccess && (
          <p className="copySuccess">
            <i className="fa-solid fa-check"></i> Đã sao chép!
          </p>
        )}
      </div>

      {content ? (
        <ReactMarkdown
          className="main-results"
          remarkPlugins={[remarkGfm]}
          rehypePlugins={[rehypeHighlight]}
        >
          {content}
        </ReactMarkdown>
      ) : loading ? (
        <i className="fa-solid fa-circle"></i>
      ) : (
        <p className="placeholder">
          Bắt đầu bằng cách điền vào biểu mẫu bên trên.
        </p>
      )}
    </div>
  );
}

export default ResultPrompt;
