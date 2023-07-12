console.log("This is an script file")

const toggleSidebar = () => {
	if($(".sidebar").is(":visible")){
		//we want to closed the sidebar
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}else{
		//we want to show 
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
};
