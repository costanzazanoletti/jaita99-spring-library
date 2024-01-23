// funzioni
async function getData() {
  const response = await fetch('http://localhost:8080/api/v1/books');
  const data = await response.json();
  console.log(data);
  // nel div content aggiungo un ul con tanti li per i book
  let content = '<ul>';
  data.forEach((book) => {
    content += '<li>' + book.title + '</li>';
  });
  content += '</ul>';
  console.log(content);
  document.getElementById('content').innerHTML = content;
}

// codice globale
getData();
