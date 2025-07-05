// components/PostDetail.jsx
import PropTypes from "prop-types";

export default function PostDetail({ post, onClose }) {
  return (
    <div role="dialog" aria-labelledby="post-title">
      <h2 id="post-title">{post.title}</h2>
      <p>{post.content}</p>
      <button onClick={onClose}>Close</button>
    </div>
  );
}

PostDetail.propTypes = {
  post: PropTypes.shape({
    id: PropTypes.string,
    title: PropTypes.string,
    content: PropTypes.string
  }).isRequired,
  onClose: PropTypes.func.isRequired
};
