export default function handler(req, res) {
  const posts = [
    { id: "1", title: "Hello, world!", content: "Welcome to my blog." },
    { id: "2", title: "Second post", content: "Some more content here." },
    { id: "3", title: "Another day", content: "Content for another day." }
  ];
  res.status(200).json(posts);
}
