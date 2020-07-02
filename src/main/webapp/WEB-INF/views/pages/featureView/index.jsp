<%@ include file="../../tiles/templates/taglibs.jsp" %>

<!-- GoJS -->
<script src="/static/js/go/go.js"></script>
<script src="/static/js/go-graphics.js"></script>

<c:forEach var="fm" items="${ featureModels }">

<div id="${ fm.getId() }">
  <div class="tree-header">
    <table class="clickable" style="width:100%">
      <tbody>
        <tr style="width:100%">
        	<c:set var = "name" value = "${fn:split(fm.getFilename() , '/')}" />
          <td style="width:95%"><span class="big-title">${ fn:substringBefore( name[fn:length(name) - 1] , '.' ) }</span></td>
          <td class="align-middle" style="width:5%" align="center"><i class="fas fa-lg fa-chevron-up"></i></td>
        </tr>
      </tbody>
    </table>
    <hr>
  </div>
</div>

</c:forEach>

<!-- Floating div -->
<div class="fixed-div">
  <!-- Go to feature's info -->
  <div id="feature-info" class="feature-info">
    <div id="feature-close" class="tree-close">
      <i class="fas fa-times"></i>
    </div>
    <b><spring:message code="views.feature.go"></spring:message>:</b> <a href="#" id="feature-info-link">RenameCodebook</a>
  </div>
  
  <!-- Zoom div -->
  <div id="tree-zoom-div" class="tree-zoom-div">
  <div id="tree-close" class="tree-close">
    <i class="fas fa-times"></i>
  </div>
  <div id="tree-zoom" class="tree-zoom"></div>
</div>
</div>




<!-- Page's JS -->
<script id="code">

var featureTrees = ${ featureTrees };
	
$(document).ready(function(){

  showFullTree(featureTrees[0].id)

  $('#tree-close').click(function(){
    $('#tree-zoom-div').fadeOut();
  })
  
  $('#feature-close').click(function(){
    $('#feature-info').fadeOut();
  })

  $('.tree-header').click(function(){
    var id = $(this).parent().attr('id')
    showFullTree(id)
  })


})

function showFullTree(divId){

  var selectedData;

  for(tree of featureTrees){

    var id = tree.id;

    if(divId != id){
      $hideTree = $('#' + id + "-ftree");
      $icon = $('#' + id).find('.fa-chevron-up')

      $hideTree
        .slideUp()

      if(! $icon.hasClass('fa-rotate-180')){
        $icon.addClass('fa-rotate-180')
      }

    }else{
      selectedData = tree;
    }
  }

  $('#tree-zoom').remove();
  $('#tree-zoom-div').append('<div id="tree-zoom" class="tree-zoom"></div>')

  $showTree = $('#' + divId + "-ftree");
  $icon = $('#' + divId).find('.fa-chevron-up')

  $showTree.slideDown()

  $icon.removeClass('fa-rotate-180')

  var $div = $('#' + divId);
  var fullId = divId + '-ftree';
  //var localId = divId + '-localDiagram';

  if($div.find(fullId)){
    // Exists! Remove
    $('#' + fullId).remove()
  }

  var $fullDiagram = $('<div id="' + divId + '-ftree" onclick="$(\'#tree-zoom-div\').fadeIn();"></div>');
  $fullDiagram
    .css('width','100%')
    .css('height','250px')

  $div.append($fullDiagram);
  createFeatureTree(fullId,"tree-zoom",selectedData.nodes,selectedData.links);
}

</script>