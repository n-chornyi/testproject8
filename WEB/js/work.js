$(document).ready(function(){	
	checkAuth();
});

function writeCookie(name, val, expires) {
  var date = new Date;
  date.setDate(date.getDate() + expires);
  document.cookie = name+"="+val+"; path=/; expires=" + date.toUTCString();
}

function readCookie(name) {
  var matches = document.cookie.match(new RegExp(
    "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
  ));
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

function addHandle(elementObject, projectId) {
	let sortable = new Sortable(elementObject, {
			group: "projects" + projectId,
			handle: '.handle',
			animation: 150,
			onEnd: function(evt) {		
				if (evt.oldIndex != evt.newIndex) {					
					let newHTMLTasks = evt.to;
					let element1 = newHTMLTasks.getElementsByClassName("row");					
					if (element1[evt.oldIndex].id != undefined && element1[evt.newIndex].id != undefined) {
						let valuePriority = "";
						for (let i=0; i<element1.length; i++) {
							valuePriority += element1[i].id.slice(element1[i].id.lastIndexOf("-")+1) + ";";
						}						
						let updateObject = {
							"indexes": valuePriority
						}			
						updateUITasks(projectId);
						url = base_url + "/project/" + projectId + "/task/update";
						$.ajax({
							url: url,
							type: "post",
							data: JSON.stringify(updateObject),
							dataType: 'json',
							contentType: "application/json; charset=utf-8",
							headers: {
								"Authorization": readCookie("token")
							},
							success: function(data) {								
							},
							error: function(xhr, status, error) {				
								errorHandler(xhr);
							}
						});					
					}		
				}
			}
		});
}

function updateUITasks(projectId) {	
	let lastTask = document.getElementById("containerTask" + projectId);
	let rows = lastTask.getElementsByClassName("col-md-10");
	for (let i=0; i< rows.length-1; i++) {
		rows[i].style.borderBottomLeftRadius = "0px";
		rows[i].style.borderBottomRightRadius = "0px";
	}
	if (rows.length >= 1) {
		rows[rows.length-1].style.borderBottomLeftRadius = "15px";
		rows[rows.length-1].style.borderBottomRightRadius = "15px";
	}	
}

function logout() {
	writeCookie('login', "", 30);
	writeCookie('token', "", 30);	
	let rows = document.body.getElementsByClassName("newBlockProject");
	while(rows[0]) {
		rows[0].remove();
	}
	toAuth();
}

function checkAuth() {	
	let token = readCookie("token");	
	if (readCookie("login") === undefined ||
		token === undefined ||
		readCookie("login") === "" ||
		token === "") {
			toAuth();
			return false;
	} 
	
	url = base_url + "/test";
	$.ajax({
		url: url,
		type: "get",
		dataType: 'html',
		headers: {
			"Authorization": token
		},
		success: function(data) {
			getProjects();
		},
		error: function(xhr, status, error) {				
			errorHandler(xhr);
		}
	});	
}

function showRegForm() {
	$('#modal-auth').modal('hide');	
	document.getElementById("regLogin").value = "";	
	document.getElementById("regPassword").value = "";
	document.getElementById("regRePassword").value = "";
	document.getElementById("regEmail").value = "";
	document.getElementById("errorReg").innerHTML = "";	
	modalRegister.show();
}

function showAuthForm() {
	$('#modal-register').modal('hide');
	document.getElementById("authLogin").value = "";	
	document.getElementById("authPassword").value = "";
	document.getElementById("errorAuth").innerHTML = "";
	toAuth();
}

function singIn() {
	let login = document.getElementById("authLogin").value;	
	let pass = document.getElementById("authPassword").value;
	
	if (!validLogin(login)) {
		document.getElementById('errorAuth').innerHTML = "Length login from 5 to 50 symbols, only letters & numbers ";
	} else if (!validPassword(pass)) {
		document.getElementById('errorAuth').innerHTML = "Length password from 6 to 50 symbols";
	} else {
		let user = {
			login: login,
			password : pass
		};
		url = base_url + "/auth/login";
			
		$.ajax({
			url: url,
			type: "post",
			data : user,
			dataType: 'json',			
			success: function(data) {			
				writeCookie('login', data['login'], 30);
				writeCookie('token', data['token'], 30);
				$('#modal-auth').modal('hide');
				getProjects();
			},
			error: function(xhr, status, error) {
				document.getElementById('errorAuth').innerHTML = "Invalid login or password";
			}
		});
	}	
}

function signUp() {
	let login = document.getElementById("regLogin").value;
	let pass = document.getElementById("regPassword").value;
	let repass = document.getElementById("regRePassword").value;
	let email = document.getElementById("regEmail").value;	
	
	if (!validLogin(login)) {
		document.getElementById('errorReg').innerHTML = "Length login from 5 to 50 symbols, only letters & numbers ";
	} else if (!validPassword(pass)) {
		document.getElementById('errorReg').innerHTML = "Length password from 6 to 50 symbols";
	} else if (pass != repass) {
		document.getElementById('errorReg').innerHTML = "Password mismatch";
	} else if (!validEmail(email)) {
		document.getElementById('errorReg').innerHTML = "Please, enter valid e-mail";
	} else {
		let user = {
			login: login,
			password : pass,
			email: email
		};
		url = base_url + "/auth/register";
			
		$.ajax({
			url: url,
			type: "post",
			data : JSON.stringify(user),
			dataType: 'json',
			contentType: "application/json; charset=utf-8",
			success: function(data) {			
				writeCookie('login', data['login'], 30);
				writeCookie('token', data['token'], 30);
				$('#modal-register').modal('hide');
				getProjects();
			},
			error: function(xhr, status, error) {
				document.getElementById('errorAuth').innerHTML = "Error register";
			}
		});
	}
	
}

function validLogin(login) {
	if (login.length<5 || login.length>50) {
		return false;
	}
	if (!login.match("^[A-Za-z0-9]+$")) {
		return false;
	}
	return true;
}

function validPassword(pass) {
	if (pass.length<6 || pass.length>50) {
		return false;
	}
	return true;
}

function validEmail(email) {
    var re = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
    return re.test(String(email).toLowerCase());
  }

function getProjects() {
	let token = readCookie("token");
	url = base_url + "/project";	
	$.ajax({
		url: url,
		type: "get",
		dataType: 'json',
		headers: {
			"Authorization": token
		},
		success: function(data) {
			for (let i = 0; i < data.length; i++) {
				url = base_url + "/project/" + data[i]['id'] + "/task";	
				$.ajax({
					url: url,
					type: "get",
					dataType: 'json',
					headers: {
						"Authorization": token
					},
					success: function(dataTask) {				
						addProjectWindow(data[i], dataTask);
					},
					error: function(xhr, status, error) {			
						errorHandler(xhr);
					}
				});
			}			
		},
		error: function(xhr, status, error) {			
			errorHandler(xhr);
		}
	});
}

function errorHandler(xhr) {
	if (xhr.status == 403) {
		toAuth();
	} else {
		toastr.options = {
		  "closeButton": false,
		  "debug": false,
		  "newestOnTop": false,
		  "progressBar": false,
		  "positionClass": "toast-top-full-width",
		  "preventDuplicates": false,
		  "onclick": null,
		  "showDuration": "300",
		  "hideDuration": "1000",
		  "timeOut": "5000",
		  "extendedTimeOut": "1000",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
		}
		toastr.warning('An error occured, please reload the page and try again')
		
	}
}

function newProject() {
	document.getElementById("textProject").value = "";
	document.getElementById("exampleModalLabelProject").innerHTML = "Add new project";	
	document.getElementById("saveProject").innerHTML = "Add";	
	document.getElementById("saveProject").setAttribute("onClick", "createProject()");
	document.getElementById("errorProject").innerHTML = "";
	const modal = new bootstrap.Modal(document.querySelector('#modal-project'));
	modal.show();
}

function createProject() {
	let projectName = document.getElementById("textProject").value;
	if (projectName.length < 5) {
		document.getElementById("errorProject").innerHTML = errorLengthProject;
	} else {
		projectJSON = { "name": projectName};
		url = base_url + "/project";
		$.ajax({
			url: url,
			type: "post",
			data: JSON.stringify(projectJSON),
			dataType: 'json',
			contentType: "application/json; charset=utf-8",
			headers: {
				"Authorization": readCookie("token")
			},
			success: function(data) {
				addProjectWindow(data)
				$('#modal-project').modal('hide');
			},
			error: function(xhr, status, error) {
				errorHandler(xhr);
			}
		});		
	}
}

function deleteProject(id) {		
	url = base_url + "/project/" + id;
	$.ajax({
		url: url,
		type: "delete",
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		headers: {
			"Authorization": readCookie("token")
		},
		success: function(data) {
		},
		error: function(xhr, status, error) {			
			if (xhr.status === 200) {
				document.getElementById("project" + id).remove();
			} else {
				errorHandler(xhr);
			}			
		}
	});
}

function toAuth() {
	modalAuth.show();
}

function editProject(id) {
	let temp = document.getElementById("saveProject");	
	temp.setAttribute("onClick", "updateProject(" + id + ")");
	document.getElementById("textProject").value = "";
	document.getElementById("exampleModalLabelProject").innerHTML = "Edit current project";
	document.getElementById("saveProject").innerHTML = "Save";
	document.getElementById("errorProject").innerHTML = "";
	const modal = new bootstrap.Modal(document.querySelector('#modal-project'));
	modal.show();	
}

function updateProject(id) {	
	const tempText = document.getElementById("textProject").value;
	if (tempText.length < 5) {
		document.getElementById("errorProject").innerHTML = errorLengthProject;
	} else {
		projectJSON = { "name": tempText};
		url = base_url + "/project/" + id;	
		
		$.ajax({
			url: url,
			type: "put",
			data: JSON.stringify(projectJSON),
			dataType: 'json',
			contentType: "application/json; charset=utf-8",
			headers: {
				"Authorization": readCookie("token")
			},
			success: function(data) {
				document.getElementById("textProject" + id).innerHTML = tempText;
				$('#modal-project').modal('hide');
			},
			error: function(xhr, status, error) {
				errorHandler(xhr);
			}
		});
		
		
	}
	
}

function editTask(projectId ,taskId) {
	let temp = document.getElementById("saveTask");	
	temp.setAttribute("onClick", "saveEditTask(" + projectId + "," + taskId + ")");
	const modal = new bootstrap.Modal(document.querySelector('#modal-task'));
	document.getElementById("titleTask").value = "";
	document.getElementById('endDateTask').value = new Date().toDateInputValue();	
	document.getElementById('statusTask').value = false;	
	document.getElementById("errorTask").innerHTML = "";
	modal.show();	
	
	url = base_url + "/project/" + projectId + "/task/" + taskId;
	$.ajax({
			url: url,
			type: "get",			
			dataType: 'json',
			contentType: "application/json; charset=utf-8",
			headers: {
				"Authorization": readCookie("token")
			},
			success: function(data) {
				document.getElementById("titleTask").value = data['title'];
				if (data['endTime'] != null) {
					document.getElementById('endDateTask').value = data['endTime'].slice(0, data['endTime'].indexOf('T'));
				}				
				document.getElementById('statusTask').checked = data['status'];
			},
			error: function(xhr, status, error) {
				errorHandler(xhr);
			}
		});	
		
	
}

function saveEditTask(projectId ,taskId) {
	let titleTask = document.getElementById("titleTask").value;
	let endDateTimeTask = document.getElementById("endDateTask").value + "T00:00:00";
	let statusTask = document.getElementById("statusTask");
	if (titleTask.length < 5) {
		document.getElementById("errorTask").innerHTML = errorLengthTask;
	} else {		
		projectJSON = { 
			"title": titleTask,
			"endTime": endDateTimeTask,
			"status": statusTask.checked
		};
		url = base_url + "/project/" + projectId + "/task/" + taskId;
		$.ajax({
			url: url,
			type: "put",
			data: JSON.stringify(projectJSON),
			dataType: 'json',
			contentType: "application/json; charset=utf-8",
			headers: {
				"Authorization": readCookie("token")
			},
			success: function(data) {
				
				if (statusTask.checked) {
					document.getElementById("taskBody" + projectId + "-" + taskId).style.background = "green";
				} else {
					document.getElementById("taskBody" + projectId + "-" + taskId).style.background = "white";
				}				
				document.getElementById("taskTitle" + projectId + "-" + taskId).innerHTML = titleTask;
				$('#modal-task').modal('hide');
			},
			error: function(xhr, status, error) {
				errorHandler(xhr);
			}
		});		
	}
	
}

function addTask(projectId) {	
	let textTask = document.getElementById("textTaskProject" + projectId).value;
	if (textTask.length < 5) {
		alert(errorLengthProject);
	} else {
		projectJSON = { "title": textTask};
		url = base_url + "/project/" + projectId + "/task";
		$.ajax({
			url: url,
			type: "post",
			data: JSON.stringify(projectJSON),
			dataType: 'json',
			contentType: "application/json; charset=utf-8",
			headers: {
				"Authorization": readCookie("token")
			},
			success: function(data) {
				document.getElementById("textTaskProject" + projectId).value = "";
				saveAddTask(projectId, data)				
			},
			error: function(xhr, status, error) {
				errorHandler(xhr);
			}
		});
	}
}

function saveAddTask(projectId, task) {
	if  (task != null) {
		let divRow3 = document.createElement("div");			
		divRow3.setAttribute("class","row");
		divRow3.setAttribute("id","task" + projectId + "-" +task["id"]);		
		
			let divTemp4_1 = document.createElement("div");
			divTemp4_1.setAttribute("class", "col-md-1");
			
			let divTemp4_2 = document.createElement("div");
			divTemp4_2.setAttribute("id","taskBody" + projectId + "-" +task["id"]);
			divTemp4_2.setAttribute("class", "col-md-10");
			divTemp4_2.setAttribute("style", "background: white; padding-top: 10px; padding-bottom: 10px; border-bottom-left-radius: 15px; border-bottom-right-radius: 15px;");
			
				let img5_1 = document.createElement("img");
				img5_1.className = "iconSize";
				img5_1.src = "img/calendar.png";
				img5_1.alt = "+";
				img5_1.style.verticalAlign = "middle";
				
				let a2 = document.createElement("a");
				a2.setAttribute("style","width: 70%; margin-left: 10px; font-size: 16px;");
				a2.setAttribute("id","taskTitle" + projectId + "-" +task["id"]);
				a2.innerHTML =task["title"];
				
				
				let img5_2 = document.createElement("img");
				img5_2.setAttribute("class", "iconSize");
				img5_2.setAttribute("id", "iconSize");
				img5_2.setAttribute("src", "img/delete.png");		
				img5_2.setAttribute("alt", "+");
				img5_2.setAttribute("onclick", "deleteTask(" + projectId + ", " +task["id"] + ")");
						
				let img5_3 = document.createElement("img");
				img5_3.setAttribute("class", "iconSize");
				img5_3.setAttribute("id", "iconSize");
				img5_3.setAttribute("src", "img/edit.png");		
				img5_3.setAttribute("alt", "+");
				img5_3.setAttribute("onclick", "editTask(" + projectId + ", " +task["id"] + ")");
						
						
				let img5_4 = document.createElement("img");
				img5_4.setAttribute("class", "iconSize handle");
				img5_4.setAttribute("id", "iconSize");
				img5_4.setAttribute("src", "img/updown.png");		
				img5_4.setAttribute("alt", "+");
				//img5_4.setAttribute("onclick", "changeStatusTask(" + projectId + ", " +task["id"] + ")");
				
				divTemp4_2.appendChild(img5_1);
				divTemp4_2.appendChild(a2);
				divTemp4_2.appendChild(img5_2);
				divTemp4_2.appendChild(img5_3);
				divTemp4_2.appendChild(img5_4);
			
			let divTemp4_3 = document.createElement("div");
			divTemp4_3.setAttribute("class", "col-md-1");
		
		divRow3.appendChild(divTemp4_1);
		divRow3.appendChild(divTemp4_2);
		divRow3.appendChild(divTemp4_3);
		
		let lastTask = document.getElementById("containerTask" + projectId);
		let rows = lastTask.getElementsByClassName("col-md-10");
		if (rows.length >= 1) {			
			rows[rows.length-1].style.borderBottomLeftRadius = "0px";
			rows[rows.length-1].style.borderBottomRightRadius = "0px";
		}
		
		lastTask.appendChild(divRow3, lastTask.lastChild);		
	}
	
}

function deleteTask(projectId ,taskId) {	
	url = base_url + "/project/" + projectId + "/task/" + taskId;
	$.ajax({
		url: url,
		type: "delete",		
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		headers: {
			"Authorization": readCookie("token")
		},
		success: function(data) {							
		},
		error: function(xhr, status, error) {			
			if (xhr.status === 200) {
				document.getElementById("task" + projectId + "-" + taskId).remove();
				let lastTask = document.getElementById("containerTask" + projectId);
				let rows = lastTask.getElementsByClassName("col-md-10");
				if (rows.length >= 1) {			
					rows[rows.length-1].style.borderBottomLeftRadius = "15px";
					rows[rows.length-1].style.borderBottomRightRadius = "15px";
				}
			} else {
				errorHandler(xhr);
			}
		}
	});	
}

function generateBlock(project, tasks) {	
	//create section
		let newSection = document.createElement("section");
		newSection.setAttribute("style","padding-top:20px;");
		newSection.id = "project" + project["id"];
		newSection.className = "newBlockProject";
		
			//create div container
			let divContainer = document.createElement("div");
			divContainer.className = "container";			
			divContainer.style.marginTop = "30px;";			
			
			
			//add container to section
			newSection.appendChild(divContainer);
			
				//create div row
				let divRow = document.createElement("div");
				divRow.className = "row";
				divContainer.appendChild(divRow);
					
					let divTemp3_1 = document.createElement("div");
					divTemp3_1.setAttribute("class", "col-md-1");
					
					
					let divFirst6 = document.createElement("div");
					divFirst6.className = "col-md-10";
					divFirst6.style.background = "linear-gradient(90deg, rgb(0, 165, 255), rgb(159, 159, 159))";
					divFirst6.id = "firstRow";
					
					let img1 = document.createElement("img");
					img1.className = "iconSize";
					img1.src = "img/calendar.png";
					img1.alt = "+";
					img1.style.verticalAlign = "middle";
					
					let a1 = document.createElement("a");
					a1.setAttribute("id","textProject" + project['id']);
					a1.setAttribute("style", "width: 70%; margin-left: 10px;");					
					a1.innerHTML = project["name"];

					let img2 = document.createElement("img");
					img2.setAttribute("class", "iconSize");
					img2.setAttribute("id", "iconSize");
					img2.setAttribute("src", "img/delete.png");
					img2.setAttribute("alt", "+");
					img2.setAttribute("onclick", "deleteProject(" + project["id"] + ")");
					
					let img3 = document.createElement("img");
					img3.setAttribute("class", "iconSize");
					img3.setAttribute("src", "img/edit.png");		
					img3.setAttribute("alt", "+");
					img3.setAttribute("onclick", "editProject(" + project["id"] + ")");				
					img3.setAttribute("id", "iconSize");
							
					divFirst6.appendChild(img1);
					divFirst6.appendChild(a1);
					divFirst6.appendChild(img2);
					divFirst6.appendChild(img3);
	
					let divTemp3_2 = document.createElement("div");
					divTemp3_2.setAttribute("class", "col-md-1");
					
				divRow.appendChild(divTemp3_1);
				divRow.appendChild(divFirst6);
				divRow.appendChild(divTemp3_2);
				
				let divRow2 = document.createElement("div");			
				divRow2.className = "row";
				divContainer.appendChild(divRow2);
				
					let divTemp3_3 = document.createElement("div");
					divTemp3_3.setAttribute("class", "col-md-1");
					
					
					let divFirst6_2 = document.createElement("div");
					divFirst6_2.className = "col-md-10";
					divFirst6_2.id = "firstRow";
					divFirst6_2.style.background = "#D3D3D3";
					
					let img4 = document.createElement("img");				
					img4.setAttribute("class","iconSize");
					img4.setAttribute("src","img/add.png");
					img4.setAttribute("alt","+");
					img4.setAttribute("style","width: 3%; vertical-align: middle;");
					
					
					let input1 = document.createElement("input");
					input1.setAttribute("id", "textTaskProject" + project["id"]);
					input1.setAttribute("type","text");
					input1.setAttribute("size","80");
					input1.setAttribute("style","width: 70%; margin-left: 10px;");
					
					let button1 = document.createElement("button");
					button1.setAttribute("class","shine-button");					
					button1.setAttribute("style","float:right; width: 100px;");
					button1.setAttribute("onClick","addTask(" + project["id"] + ")");
					button1.innerHTML = "Add task";
							
					divFirst6_2.appendChild(img4);
					divFirst6_2.appendChild(input1);
					divFirst6_2.appendChild(button1);					
	
					let divTemp3_4 = document.createElement("div");
					divTemp3_4.setAttribute("class", "col-md-1");
					
					divRow2.appendChild(divTemp3_3);
					divRow2.appendChild(divFirst6_2);
					divRow2.appendChild(divTemp3_4);
					
				
				let handleContainer = document.createElement("div");
				handleContainer.className = "container";
				handleContainer.id = "containerTask" + project["id"];
				handleContainer.style.marginTop = "30px;";			
				addHandle(handleContainer,  project["id"]);					
				newSection.appendChild(handleContainer);
				
				if  (tasks != null && tasks != undefined) {
					for (let j=0; j<tasks.length; j++) {
						let divRow3 = document.createElement("div");			
						divRow3.setAttribute("class","row");
						divRow3.setAttribute("id","task" + project["id"] + "-" + tasks[j]["id"]);						
						handleContainer.appendChild(divRow3);
						
							let divTemp4_1 = document.createElement("div");
							divTemp4_1.setAttribute("class", "col-md-1");
							
							let divTemp4_2 = document.createElement("div");
							divTemp4_2.setAttribute("id","taskBody" + project["id"] + "-" + tasks[j]["id"]);
							divTemp4_2.setAttribute("class", "col-md-10");
							
							
							if ((j+1) === tasks.length) {		
								divTemp4_2.setAttribute("style", "background: white; padding-top: 10px; padding-bottom: 10px; border-bottom-left-radius: 15px; border-bottom-right-radius: 15px;");
							} else {
								divTemp4_2.setAttribute("style","background: white; padding-top: 10px; padding-bottom: 10px; ");
							}
							
							if (tasks[j]['status']) {
								divTemp4_2.style.background = "green";
							} else {
								divTemp4_2.style.background = "white";
							}
							
								let img5_1 = document.createElement("img");
								img5_1.className = "iconSize";
								img5_1.src = "img/calendar.png";
								img5_1.alt = "+";
								img5_1.style.verticalAlign = "middle";
								
								let a2 = document.createElement("a");
								a2.setAttribute("style","width: 70%; margin-left: 10px; font-size: 16px;");								
								a2.setAttribute("id","taskTitle" + project["id"] + "-" + tasks[j]["id"]);
								a2.innerHTML = tasks[j]["title"];
								
								
								let img5_2 = document.createElement("img");
								img5_2.setAttribute("class", "iconSize");
								img5_2.setAttribute("id", "iconSize");
								img5_2.setAttribute("src", "img/delete.png");		
								img5_2.setAttribute("alt", "+");
								img5_2.setAttribute("onclick", "deleteTask(" + project["id"] + ", " + tasks[j]["id"] + ")");
										
								let img5_3 = document.createElement("img");
								img5_3.setAttribute("class", "iconSize");
								img5_3.setAttribute("id", "iconSize");
								img5_3.setAttribute("src", "img/edit.png");		
								img5_3.setAttribute("alt", "+");
								img5_3.setAttribute("onclick", "editTask(" + project["id"] + ", " + tasks[j]["id"] + ")");
										
										
								let img5_4 = document.createElement("img");
								img5_4.setAttribute("class", "iconSize handle");
								img5_4.setAttribute("id", "iconSize");
								img5_4.setAttribute("src", "img/updown.png");		
								img5_4.setAttribute("alt", "+");
								img5_4.setAttribute("onClick", "changeStatusTask(" + project["id"] + ", " + tasks[j]["id"] + ")");
										
								
								divTemp4_2.appendChild(img5_1);
								divTemp4_2.appendChild(a2);
								divTemp4_2.appendChild(img5_2);
								divTemp4_2.appendChild(img5_3);
								divTemp4_2.appendChild(img5_4);

							
							let divTemp4_3 = document.createElement("div");
							divTemp4_3.setAttribute("class", "col-md-1");
						
						divRow3.appendChild(divTemp4_1);
						divRow3.appendChild(divTemp4_2);
						divRow3.appendChild(divTemp4_3);
					}
				}
				
			
		return newSection;
}

function addProjectWindow(projects, tasks) {
	let temp = generateBlock(projects, tasks);
	addBlock(temp);
}
	
function addBlock(newSection) {
	let add = document.getElementById("add");
	document.body.insertBefore(newSection, add); 
}

Date.prototype.toDateInputValue = (function() {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0,10);
});
