import { getServerSideProps } from "../pages/posts";

describe("getServerSideProps", () => {
  const mockPosts = [
    { id: "1", title: "X", content: "Y" }
  ];

  beforeAll(() => {
    global.fetch = jest.fn(() =>
      Promise.resolve({ json: () => Promise.resolve(mockPosts) })
    );
  });

  it("fetches posts and returns them as props", async () => {
    const result = await getServerSideProps();
    expect(global.fetch).toHaveBeenCalledWith("http://localhost:3000/api/posts");
    expect(result).toEqual({ props: { posts: mockPosts } });
  });
});
