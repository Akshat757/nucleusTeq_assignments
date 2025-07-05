import { render, screen, fireEvent } from "@testing-library/react";
import PostList from "../components/PostList";

describe("PostList", () => {
  const mockPosts = [
    { id: "1", title: "First", content: "A" },
    { id: "2", title: "Second", content: "B" }
  ];

  it("renders a button for each post and calls onSelect when clicked", () => {
    const onSelect = jest.fn();
    render(<PostList posts={mockPosts} onSelect={onSelect} />);

    const firstBtn = screen.getByText("First");
    fireEvent.click(firstBtn);

    expect(onSelect).toHaveBeenCalledTimes(1);
    expect(onSelect).toHaveBeenCalledWith(mockPosts[0]);

    const secondBtn = screen.getByText("Second");
    fireEvent.click(secondBtn);

    expect(onSelect).toHaveBeenCalledWith(mockPosts[1]);
  });
});
