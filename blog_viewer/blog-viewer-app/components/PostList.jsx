// components/PostList.jsx
import PropTypes from "prop-types";

export default function PostList({ posts, onSelect }) {
  return (
    <ul role="list">
      {posts.map((post) => (
        <li key={post.id}>
          <button onClick={() => onSelect(post)}>{post.title}</button>
        </li>
      ))}
    </ul>
  );
}

PostList.propTypes = {
  posts: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string,
      title: PropTypes.string,
      content: PropTypes.string
    })
  ).isRequired,
  onSelect: PropTypes.func.isRequired
};
