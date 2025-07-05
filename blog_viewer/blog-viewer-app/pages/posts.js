import { useState } from "react";
import PostList from "../components/PostList";
import PostDetail from "../components/PostDetail";

export async function getServerSideProps() {
  const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL || "http://localhost:3000"}/api/posts`);
  const posts = await res.json();
  return { props: { posts } };
}

export default function PostsPage({ posts }) {
  const [selected, setSelected] = useState(null);

  if (!posts) {
    return <p>Loading…</p>;
  }

  return (
    <div>
      <h1>Blog Posts</h1>
      <PostList posts={posts} onSelect={setSelected} />
      {selected && <PostDetail post={selected} onClose={() => setSelected(null)} />}
    </div>
  );
}
