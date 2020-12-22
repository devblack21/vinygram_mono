var pagina = 1;

function showLoading() {
	loading = document.querySelector('.loading');
	loading.classList.add('show');
	setTimeout(addImageToDOM,900)
}

window.addEventListener(
				'scroll',
				function() {
					
					
					loading = document.querySelector('.loading');

					if (window.scrollY + window.innerHeight >= document.documentElement.scrollHeight) {
						pagina++;
										
						showLoading();
					}
})

function input_upload(upload){
	   document.getElementById(upload).click();
}

function refreshDataList(){

	 var list = document.getElementById('users');
	 campo = document.getElementById('pesquisa')
	 list.innerHTML = '';
	url_ = window.location.protocol + "//"+window.location.host; 
	
	
	 url_ = url_+"/user/users?username="+campo.value;
	 
	 $.ajax({
	        type: "get",
	        url: url_,
	        success: function (response) {
	        	
	        	var lista = response;
	        	
	        	for(indice in lista){
	        		
	        		var opt = document.createElement('option');
	        		 
	        		 opt.value = lista[indice];
	        		list.appendChild(opt);
	        	}

	        },
	        error: function (xhr, ajaxOptions, thrownError) {
	            alert(thrownError);
	        }
	 });
}


function validateFileType(upload, imagem) {
	var fileName = document.getElementById(upload).value;
	var img = document.getElementById(imagem);
	var idxDot = fileName.lastIndexOf(".") + 1;
	var extFile = fileName.substr(idxDot, fileName.length)
			.toLowerCase();

	var reader = new FileReader();
	var file = document.getElementById(upload).files[0];
	if (extFile == "jpg") {

		reader.onloadend = function() {
			img.src = reader.result;
			
		}

		if (file) {
			reader.readAsDataURL(file);
		} else {
			img.src = "https://res.cloudinary.com/devblack/image/upload/users/sem-foto.jpg";
			//txt_upload.innerHTML = "Escolher Imagem...";
		}

	} else {
		alert("Somente arquivos jpg / jpeg e png são permitidos!");
		fileName.value = null;
		img.src = "https://res.cloudinary.com/devblack/image/upload/users/sem-foto.jpg";
		//txt_upload.innerHTML = "Escolher Imagem...";
	}
}

function embed(){
	
	var site = document.getElementById("site").value;
	var codigo = document.getElementById("codigo").value;
	
	var embed = document.getElementById("embed");
	embed.src = 'https://'+site+'/embed/'+codigo;
}

function modal_view(elemento) {

			var nameSpan = '';
	
			switch (elemento) {
			case 'PROFILE':
				$('#modalPerfil').modal('show');
				break;
			
			case 'TXT':
				$('#modalTXT').modal('show');
				break;
			case 'VIDEO':
				$('#modalVIDEO').modal('show');	
				nameSpan = 'span_view_video';
				break;
			case 'IMG':
				$('#modalIMG').modal('show');
				nameSpan = 'span_view_image';
				break;
		
			default:
				break;
			}
}

$(document).ready(function(){
	
	$("#modalVIDEO").on('hide.bs.modal', function(){
	    $("#embed").attr('src', '');
	});
});

function cad_user() {

 
	var $form = $("#cadUser");
	var senha1 = document.getElementById("password").value;
    var senha2 = document.getElementById("confirmaPassword").value;
    var erro = document.getElementById("er");

    
    if((senha1 == senha2)){
    	
			$form.submit();
			
    }else
	{
    	addClass('password','is-invalid');
    	addClass('confirmaPassword','is-invalid');
		erro.innerHTML = "As senhas não se conhecidem";
	}

}

function addClass(id, classe) {
	
	  var elemento = document.getElementById(id);
	  var classes = elemento.className.split(' ');
	  var getIndex = classes.indexOf(classe);

	  if (getIndex === -1) {
	    classes.push(classe);
	    elemento.className = classes.join(' ');
	  }
	}

function delClass(id, classe) {
	  var elemento = document.getElementById(id);
	  var classes = elemento.className.split(' ');
	  var getIndex = classes.indexOf(classe);

	  if (getIndex > -1) {
	    classes.splice(getIndex, 1);
	  }
	  elemento.className = classes.join(' ');
}

  window.addEventListener('load', function() {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementById('saveProfile');
    // Loop over them and prevent submission
    var validation = Array.prototype.filter.call(forms, function(form) {
    	
    
      form.addEventListener('submit', function(event) {
    		alert("adf");
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('is-invalid');
      }, false);
    });
  })

  
  function showFormError(errorVal) {
		//show error messages that comming from backend and change border to red.
		for(var i=0; i < errorVal.length; i++) {
			if(errorVal[i].fieldName === 'EMAIL') {
				clearForm();
				EmailField.attr("placeholder", errorVal[i].message).css("border", " 1px solid red");
			}
				
			else if(errorVal[i].fieldName === 'PASSWORD'){
				PasswordField.val('');
				PasswordField.attr("placeholder", errorVal[i].message).css("border", " 1px solid red");
			}
			else if(errorVal[i].fieldName === 'FORM FAIL'){
				clearForm();
				GeneralErrorField.css("display", "block").html(errorVal[i].message);
			}
		}
	}
  
  
  function followOrUnfollow(id) {

		
		var $form = $("#followOrUnfollow");	

		$.ajax({

			url : $form.attr('action')+"?idUserFollow="+id,
			type : 'GET',
			data : $form.serialize(),

			success : function(response) {
				if ($(response).find('.has-error').length) {
					
					
				} else {		
						refreshProfile(id);	
				}
			}
		});
	}
  
  function likeOrDeslike(id) {

	  url_ = window.location.protocol + "//"+window.location.host; 
	  url_ = url_ + "/post/likeOrDeslike?idPostLike="+id;

		$.ajax({

			url : url_,
			type : 'GET',
		

			success : function(response) {
		
					var resposta = response;
					span = document.getElementById('post-span-'+id);
					button = document.getElementById('post-svg-'+id);
					
					deslike = button.getElementsByClassName('icon-deslike');
					like = button.getElementsByClassName('icon-like');
					
					if(button.classList.contains('icon-deslike')){
						
						button.classList.add('icon-like');
						button.classList.remove('icon-deslike');
						
					}else
					if(button.classList.contains('icon-like')){
						
						button.classList.add('icon-deslike');
						button.classList.remove('icon-like');
					}
						
					
					
					span.innerHTML = resposta;

			}
		});

	}
 
 function feed(id,profile){
	 
	
	 
	 url_ = window.location.protocol + "//"+window.location.host;
	 
	url_ = url_+ "/post/feedlist/?user="+id+"&page="+pagina+"&profile="+profile;	


	$.ajax({

		url : url_,
		type : 'GET',
	

		success : function(response) {
			if ($(response).find('.has-error').length) {
				
				
			} else {		
				
				const imageDiv = document.createElement('div');
				console.log("resposta: "+response);
				if(response == null || response == ""){
					console.log("nulo");
					pagina--;
				}
				
				imageDiv.innerHTML = response;
				document.querySelector('.container').appendChild(imageDiv);

				loading.classList.remove('show');
			}
		}
	});
	
	 
 } 
  
  
  function notification(){
	 
	
	 
	 url_ = window.location.protocol + "//"+window.location.host;
	 
	url_ = url_+ "/notification/listscroll/?page="+pagina;	


	$.ajax({

		url : url_,
		type : 'GET',
	

		success : function(response) {
			if ($(response).find('.has-error').length) {
				
				
			} else {		
				
				const imageDiv = document.createElement('div');
				console.log("resposta: "+response);
				if(response == null || response == ""){
					console.log("nulo");
					pagina--;
				}
				
				imageDiv.innerHTML = response;
				document.querySelector('.noti').appendChild(imageDiv);

				loading.classList.remove('show');
			}
		}
	});
	
	 
 } 
  
	
function refreshProfile(id) {

		url_ = window.location.protocol + "//"+window.location.host;
		url_ = url_ + "/user/profileRefresh?idProfile="+id;
		
		var $form = $("#formperfil");	

		$.ajax({

			url : url_,
			type : 'GET',
			data : $form.serialize(),

			success : function(response) {
				if ($(response).find('.has-error').length) {
					
					$form.html(response);
					
				} else {
					
					$("#cardPerfil").html(response);
					
				}
			}
		});

	};
	
	
	
	
	
