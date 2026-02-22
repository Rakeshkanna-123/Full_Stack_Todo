const SERVER_URL = "http://localhost:8081";


function login(){
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    
    fetch(`${SERVER_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        body: JSON.stringify({
            email : email,
            password : password
        })
    })
    .then(response => {
        if(!response.ok){
            throw new Error(response.message || "kindly sign up !");
        }
        return response.json();
    })
    .then(data => {
        localStorage.setItem("token", data.token);
        window.location.href = "index.html";

    })
    .catch(error => {
        alert(error.message);
    })

}

function signUp(){
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    
    fetch(`${SERVER_URL}/auth/sign_up`, {
        method: "POST",
        headers: {
            "content-type" : "application/json"
        },
        body: JSON.stringify({
            email : email, 
            password : password
        })
    })
    .then(response => {
        if(response.ok){
            alert("Successfully Signed Up !, kindly login to continue!")
            window.location.href = "login.html"
        }else{
            return response.json().then(data => {throw new Error(data.message || "X Failed to Sign up")});
        }
    }).catch(error => {
        alert(error.message);
    })
}

function createTodoCard(todo){
    const card = document.createElement("div");
    card.className = "todo-card";
    //created styling in css with class name todo-card so to apply the styling we declare that card belongs to to-do class

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.className = "todo-checkbox";
    checkbox.checked = todo.completed;
    checkbox.addEventListener("change", function(){
        const updatedTodo = {...todo, completed : checkbox.checked};
        updateTodoStatus(updatedTodo);
    });

    const span = document.createElement("span");
    span.textContent = todo.title;
    if(todo.completed){
        span.style.textDecoration = "line-through";
        span.style.color = "#aaa";
    }

    //this will create a edit option when double clicked
    span.addEventListener("dblclick", function(){
        const input = document.createElement("input");
        input.type = "text";
        input.value = span.textContent;
        input.className = "edit-input";

        span.replaceWith(input);
        input.focus();

        function saveEdit(){
            const newTitle = input.value.trim();
            if(newTitle && newTitle !== todo.title){
                todo.title = newTitle;
                updateTodoTitle(todo);
            }
            span.textContent = newTitle || todo.title;
            input.replaceWith(span);
        }

        input.addEventListener("blur", saveEdit);
        input.addEventListener("keypress", function(e){
            if(e.key == "Enter"){
                saveEdit();
            }
        })

    })

    // Expand/Collapse button
    const expandBtn = document.createElement("button");
    expandBtn.textContent = "▼";
    expandBtn.className = "expand-btn";
    expandBtn.onclick = function(){
        const description = card.querySelector(".todo-description");
        if(description.style.display === "none" || description.style.display === ""){
            description.style.display = "block";
            expandBtn.textContent = "▲";
        } else {
            description.style.display = "none";
            expandBtn.textContent = "▼";
        }
    };

    

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "X";
    deleteBtn.className = "delete-btn";
    deleteBtn.onclick = function(){
        deleteTodo(todo.id);
    };

    // Description element (hidden by default)
    const description = document.createElement("div");
    description.className = "todo-description";
    description.textContent = todo.description || "No description";
    description.style.display = "none";

    card.appendChild(checkbox);
    card.appendChild(span);
    card.appendChild(expandBtn);
    card.appendChild(deleteBtn);
    card.appendChild(description);

    return card;
    
}

function updateTodoTitle(todo){
    const token = localStorage.getItem("token");
    fetch(`${SERVER_URL}/api/v1/todo`, {
        method : "PUT",
        headers : {
            Authorization : `Bearer ${token}`,
            "Content-Type" : "application/json"
        },
        body : JSON.stringify(todo)
    }) 
    .then(response => {
        if(!response.ok){
            throw new Error("Failed to update!");
        }
        return response.json();
    })
    .then( () => loadTodos())
    .catch(error => {
        alert(error.message);
    })
}

function loadTodos(){
    const token = localStorage.getItem("token") ;
    if(!token){
        alert("Kindly login to continue!")
        window.location.href = "login.html";
    }

    fetch(`${SERVER_URL}/api/v1/todo/userTodo`, {
        method : "GET",
        headers : {
            Authorization : `Bearer ${token}`,
        }
    })
    .then(response => {
        if(!response.ok){
            throw new Error( response.message || "Failed to get Todos!");
        }
        return response.json();
    })
    .then( (todos) => {
        const todoList = document.getElementById("todo-list");
        todoList.innerHTML = ""; // clearing the already existing todos because after loading i need updated todos from the database

        if(!todos || todos.length === 0){
            todoList.innerHTML = `<p id = "empty">No Todos Created</p>`;
        }else{
            todos.forEach(todo => {
                todoList.appendChild(createTodoCard(todo));
            });
        }
    })
    .catch(error => {
        document.getElementById("todo-list").innerHTML = `<p style : "color:red">Failed to load Todos<p>`;
    })
}

function addTodo(){
    const token = localStorage.getItem("token") ;
    const input1 = document.getElementById("new-todo");
    const todoText = input1.value.trim();
    const input2 = document.getElementById("description");
    const todoDesc = input2.value.trim();

    fetch(`${SERVER_URL}/api/v1/todo/create`, {
        method : "POST",
        headers : {
            Authorization : `Bearer ${token}`,
            "Content-Type" : "application/json"
        },
        body : JSON.stringify({
            title : todoText,
            description : todoDesc,
            completed : false 
        })
    })
    .then(response => {
        if(!response.ok){
            throw new Error(response.message || "Failed to create Todo!");
        }
        return response.json();
    })
    .then( () => loadTodos())
    .catch(error => {
        alert(error.message)
    })
    input1.value = "";
    input2.value = "";

}

function deleteTodo(id){
    const token = localStorage.getItem("token") ;
    fetch(`${SERVER_URL}/api/v1/todo/${id}`,{
        method : "DELETE",
        headers : { Authorization : `Bearer ${token}`}, //it will delete only the token matches, content-type not need as it does not return json format  
    })
    .then(response => {
        if(!response.ok){
            throw new Error(response.message || "Failed to delete Todo!");
        }
        return response.text();
    })
    .then( () => loadTodos())
    .catch(error => {
        alert(error.message);
    })

}

function updateTodoStatus(todo){
    console.log(todo);
    const token = localStorage.getItem("token") ;
    fetch(`${SERVER_URL}/api/v1/todo`, {
        method : "PUT",
        headers : {
            "Authorization" : `Bearer ${token}`,
            "Content-Type" : "application/json"
        },
        body : JSON.stringify(todo)
    })
    .then(response => {
        if(!response.ok){
            throw new Error(response.message || "Failed to update Todo");
        }
        return response.json();
    })
    .then( () => loadTodos())
    .catch(error => {
        alert(error.message);
    })

}

function deleteAllTodos(){
    const token = localStorage.getItem("token");
    fetch(`${SERVER_URL}/api/v1/todo`, {
        method : "DELETE",
        headers : { Authorization : `Bearer ${token}`}
    })
    .then(response => {
        if(!response.ok){
            throw new Error(response.message || "Failed to clear Todos");
        }
        return response.text;
    })
    .then( () => loadTodos())
    .catch(error => {
        alert(error.message);
    })
}

const newTodoInput = document.getElementById("new-todo");
if(newTodoInput){
    newTodoInput.addEventListener("keypress", function(e){
        if(e.key === "Enter"){
            addTodo();
        }
    });
}

document.addEventListener("DOMContentLoaded", function (){
    if(document.getElementById("todo-list")){
        loadTodos();
    }
})