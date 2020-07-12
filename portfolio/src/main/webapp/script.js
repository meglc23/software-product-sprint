/** Fetches comments from the server and adds them to the DOM. */
function loadComments() {
  fetch('/list-comments').then(
      response => response.json()).then(
          comments => {
            const commentListElement = document.getElementById('list-comments');
            comments.forEach((comment) => {
                commentListElement.appendChild(createCommentElement(comment));
            })
      });
}

/** Creates an element that represents a comment. */
function createCommentElement(comment) {
  const commentElement = document.createElement('div');
  commentElement.className = 'single comment-item';

  const contentElement = document.createElement('p');
  contentElement.innerText = comment.content;

  commentElement.appendChild(contentElement);
  return commentElement;
}

