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

/** Creates an element that represents a comment. */
function createCommentElement(comment) {
  const commentElement = document.createElement('div');
  commentElement.className = 'single comment-item';

  const contentElement = document.createElement('p');
  contentElement.innerText = comment.content;

  // The delete button is set to be hidden.
  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.className = "hidden-element";
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.addEventListener('click', () => {
    deleteComment(comment);
    commentElement.remove();
  });

  commentElement.appendChild(contentElement);
  commentElement.appendChild(deleteButtonElement);

  return commentElement;
}

/** Tells the server to delete the comment.
 *  This function is for testing purpose.
 */
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-comment', {method: 'POST', body: params});
}

/** Request translation of all comments to the language chosen by user. */
function requestTranslation() {
  const commentElements = document.getElementsByClassName('comment-item');
  const languageCode = document.getElementById('language').value;
  const params = new URLSearchParams();
  params.append('languageCode', languageCode);
  params.append('commentLength', commentElements.length);

  for (var i = 0; i < commentElements.length; i++) {
    params.append(i, commentElements[i].innerText);
  }

  fetch('/translate', {
    method: 'POST',
    body: params
  }).then(response => response.json())
  .then((translatedComments) => {
    for (var i = 0; i < commentElements.length; i++) {
      commentElements[i].innerText = translatedComments[i];
    }
  });
}