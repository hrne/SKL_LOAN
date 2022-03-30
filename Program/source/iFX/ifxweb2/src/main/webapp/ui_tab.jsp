<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>


<script src="script/fixIE.js"></script>
<script src="script/jquery.js"></script>
<script src="script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="script/ui_tab.js"></script>
<link rel=stylesheet type=text/css
	href="script/jqueryui/css/smoothness/jquery-ui-1.10.3.custom.css" />
<style>
#dialog label,#dialog input {
	display: block;
}

#dialog label {
	margin-top: 0.5em;
}

#dialog input,#dialog textarea {
	width: 95%;
}

#tabs {
	margin-top: 1em;
}

#tabs li .ui-icon-close {
	float: left;
	margin: 0.4em 0.2em 0 0;
	cursor: pointer;
}

#add_tab {
	cursor: pointer;
}
</style>

<script>
	</script>



<div class="demo">

	<div id="dialog" title="Tab data">
		<form>
			<fieldset class="ui-helper-reset">
				<label for="tab_title">Title</label> <input type="text"
					name="tab_title" id="tab_title" value=""
					class="ui-widget-content ui-corner-all" /> <label
					for="tab_content">Content</label>
				<textarea name="tab_content" id="tab_content"
					class="ui-widget-content ui-corner-all"></textarea>
			</fieldset>
		</form>
	</div>

	<button id="add_tab">Add Tab</button>

	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">Nunc tincidunt</a> <span
				class="ui-icon ui-icon-close">Remove Tab</span></li>
		</ul>
		<div id="tabs-1">
			<p>Proin elit arcu, rutrum commodo, vehicula tempus, commodo a,
				risus. Curabitur nec arcu. Donec sollicitudin mi sit amet mauris.
				Nam elementum quam ullamcorper ante. Etiam aliquet massa et lorem.
				Mauris dapibus lacus auctor risus. Aenean tempor ullamcorper leo.
				Vivamus sed magna quis ligula eleifend adipiscing. Duis orci.
				Aliquam sodales tortor vitae ipsum. Aliquam nulla. Duis aliquam
				molestie erat. Ut et mauris vel pede varius sollicitudin. Sed ut
				dolor nec orci tincidunt interdum. Phasellus ipsum. Nunc tristique
				tempus lectus.</p>
		</div>
	</div>

</div>
<!-- End demo -->



<div class="demo-description">
	<p>Simple tabs adding and removing.</p>
</div>
<!-- End demo-description -->
</body>
</html>