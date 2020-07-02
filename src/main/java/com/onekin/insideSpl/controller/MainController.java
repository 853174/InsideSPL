package com.onekin.insideSpl.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.onekin.insideSpl.businessLogic.CMapBLInterface;
import com.onekin.insideSpl.businessLogic.FeatureBLInterface;
import com.onekin.insideSpl.businessLogic.GenericBLInterface;
import com.onekin.insideSpl.businessLogic.ProductBLInterface;
import com.onekin.insideSpl.domain.SPL;
import com.onekin.insideSpl.domain.cmap.CMap;

@Controller
public class MainController {
	
	@Autowired
	private GenericBLInterface genericBL;
	
	@Autowired
	private FeatureBLInterface featureBL;
	
	@Autowired
	private ProductBLInterface productBL;
	
	@Autowired
	private CMapBLInterface cmapBL;
	
	public static HttpSession session() {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(true); // true == allow create
	}

	@RequestMapping(value="")
    public String index(Model model) {
		
		List<SPL> spls = genericBL.getAllSpl();
		
		if(session().getAttribute("selectedSplId") != null){
			String id = (String) session().getAttribute("selectedSplId");
			SPL spl = genericBL.getSplById(id);
			
			if(spl == null) {
				// Invalidate
				changeSpl(model);
			}
			
			List<CMap> cmaps = cmapBL.getCMapsOfSpl(id);
				
			model.addAttribute("selectedSpl",spl);
			model.addAttribute("cmaps",cmaps);
		}
		
		Map<String,Integer> splInfo = new HashMap<String, Integer>();
		
		for(SPL s : spls) {
			int featureCount = featureBL.getAllFeaturesOfSpl(s.getId()).size();
			int productCount = productBL.getAllProductsOfSpl(s.getId()).size();
			splInfo.put(s.getId() + "_featureCount", featureCount);
			splInfo.put(s.getId() + "_productCount", productCount);
		}
		
		model.addAttribute("splList",spls);
		model.addAttribute("splInfo",splInfo);
		
        return "index";
    }
	
	@RequestMapping(value="/{splId}")
	public String splIndex(Model model, @PathVariable(name = "splId",required = true) String id) {
		
		SPL spl = genericBL.getSplById(id);
		
		if( spl != null) // Only save if it's correct
			session().setAttribute("selectedSplId", spl.getId());
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/changeSpl")
	public String changeSpl(Model model) {
		
		session().invalidate();
		
		return "redirect:/";
	}
	
	// LOCALE  UTILITY
	
	@Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;
	
	@RequestMapping(value="/locales/{springCode}")
	@ResponseBody
	public String localeResolver(@PathVariable(name = "springCode",required = true) String code) {
		
		Locale locale = LocaleContextHolder.getLocale();
		
		code = code.replaceAll("-", ".");
				
		try {
			// Return string associated to code on the current language
			return messageSource.getMessage(code, null, locale);
		}catch(Exception e) {
			// Not defined code
			return "*" + code + "*";
		}
		
	}

}
