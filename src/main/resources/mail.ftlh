<#ftl auto_esc=false>
<!DOCTYPE html>
<div style='background-color: rgba(128, 119, 86, 0.2)'>
<h2 style='color: rgb(128, 119, 86); background-color: black; padding: 10px'>${websiteHeader}&nbsp;${website}</h2>
<table style='width: 100%'>
  <tr><td style='padding: 10px'>
    <h3>${emailAddressLabel}:<h3
  </td></tr>
  <tr><td style='-left: 10px'>
	  <div style='border:1px solid black;padding-left: 10px'>
	  ${emailAddress}
	  </div>
  </td></tr>
  
  <tr><td><hr></td></tr>
  
  <tr><td style='padding: 10px'>
	  <h3>${requestLabel}:<h3>
  </td></tr>
  <tr><td style='padding-left: 10px'>
	  <div style='border:1px solid black; padding: 10px'>
	  ${request}   
	  </div>
  </td></tr>
  <tr><td><hr></td></tr>
</table>
</div>
