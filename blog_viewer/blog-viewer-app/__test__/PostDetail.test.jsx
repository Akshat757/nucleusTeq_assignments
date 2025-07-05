import { render, screen, fireEvent } from "@testing-library/react";
import PostDetail from "../components/PostDetail";

describe("PostDetail", () => {
  const post = { id: "1", title: "Title", content: "Body" };

  it("displays title, content, and calls onClose when Close clicked", () => {
    const onClose = jest.fn();
    render(<PostDetail post={post} onClose={onClose} />);

    expect(screen.getByRole("heading", { name: "Title" })).toBeInTheDocument();
    expect(screen.getByText("Body")).toBeInTheDocument();

    fireEvent.click(screen.getByText("Close"));
    expect(onClose).toHaveBeenCalledTimes(1);
  });
});
