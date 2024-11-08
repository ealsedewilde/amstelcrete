<%@ page import="java.util.Locale, java.util.ResourceBundle"%>
	<%
	ResourceBundle bundle = null;
	if (request.getServerName().indexOf(".nl") > -1) {
	  bundle = ResourceBundle.getBundle("bundle", new Locale("nl", "NL"));
	} else {
	  bundle = ResourceBundle.getBundle("bundle", Locale.US);
	} ;
	%>
<!DOCTYPE html>
<html lang='<%=bundle.getString("lang")%>'>
<head>
<title>Amstel Crete</title>
<link rel="icon" type="image/png" href="images/amstelcretefav.png">
<link rel="stylesheet" type="text/css" href="main.css">
<script
	src="https://www.google.com/recaptcha/enterprise.js?render=6LdJNmwqAAAAAL8TsoUd1B_vZW27FovrXCSA0RcQ"></script>
<script>
	  function onClick() {
		  document.getElementById('em').innerHTML = ''; 
				if (validate()) {
				    grecaptcha.enterprise.ready(async () => {
					      const token = await grecaptcha.enterprise.execute('6LdJNmwqAAAAAL8TsoUd1B_vZW27FovrXCSA0RcQ', {action: 'info_request'});
					      console.log("token ",token);
							document.getElementById("captchatoken").value = token;
							document.getElementById("ac-form").submit();
					      });
				} else {
					document.getElementById('em').innerHTML = '<%=bundle.getString("inputError")%>'; 
				}
		}
	  function validate() {
			const re = /^[\w]{1,}[\w.+-]{0,}@[\w-]{2,}([.][a-zA-Z]{2,}|[.][\w-]{2,}[.][a-zA-Z]{2,})$/
		  let emailValue = document.getElementById("email").value;
			let result1 = re.test(emailValue);
			if (result1) {
				document.getElementById("emailE").innerHTML = "";
			} else {
				document.getElementById("emailE").innerHTML = "<%=bundle.getString("invalidInput")%>";
			}
		  let messageValue = document.getElementById("inputMessage").value;
		  let result2 = messageValue.length >= 80 && messageValue.length <= 800;
			if (result2) {
				document.getElementById("inputMessageE").innerHTML = "";
			} else {
				document.getElementById("inputMessageE").innerHTML = "<%=bundle.getString("invalidInput")%>";
			}
		  return result1 && result2;
		}
	</script>
</head>
<body>
	<form id="ac-form" method="post" action="contact">
		<input type="hidden" id="captchatoken" name="captchatoken" size="1"
			value="" />
		<table>
			<tr>
				<td></td>
				<td><h1><%=bundle.getString("welcome")%></h1> <img alt=""
					src="images/amstelcrete_logo.jpg"></td>
			</tr>
			<tr>
				<td></td>
				<td><h3><%=bundle.getString("contact")%></h3></td>
			</tr>
			<tr>
				<td><label for="email"><%=bundle.getString("labelEmail")%></label><span
					class="red">&nbsp;*&nbsp;</span></td>
				<td><input type="email" id="email" name="email" size="81"
					pattern="^[\w]{1,}[\w.+-]{0,}@[\w-]{2,}([.][a-zA-Z]{2,}|[.][\w-]{2,}[.][a-zA-Z]{2,})$"
					required placeholder=""></td>
				<td><span id="emailE" class="red"></span></td>
			</tr>
			<tr>
				<td><label for="inputMessage"><%=bundle.getString("labelMessage")%></label><span
					class="red">&nbsp;*&nbsp;</span></td>
				<td><textarea id="inputMessage" name="inputMessage" rows="10" cols="80" required
						placeholder="<%=bundle.getString("placeholder")%>" minlength="80"
						maxlength="800"></textarea></td>
				<td><span id="inputMessageE" class="red"></span></td>
			</tr>
			<tr>
				<td></td>
				<td><%=bundle.getString("infoText")%> <span class="red">&nbsp;*&nbsp;</span></td>
			</tr>
			<tr>
				<td></td>
				<td>
					<button class="g-recaptcha" onclick="onClick()"
						data-sitekey="6LdJNmwqAAAAAL8TsoUd1B_vZW27FovrXCSA0RcQ"
						data-action='info_request'><%=bundle.getString("labelSubmit")%>
					</button>
					<span id="em" class="red"></span>
				</td>
			</tr>
			<%
			if ("showError".equals(session.getAttribute("messageType"))) {
			  out.write("<tr><td></td><td class=\"red\">");
			  out.write(bundle.getString((String) session.getAttribute("messageKey")));
			  out.write("</td></tr>");
			} else if ("showInfo".equals(session.getAttribute("messageType"))) {
			  out.write("<tr><td></td><td class=\"green\">");
			  out.write(bundle.getString((String) session.getAttribute("messageKey")));
			  out.write("</td></tr>");
			}
			%>
		</table>
	</form>

</body>
</html>
