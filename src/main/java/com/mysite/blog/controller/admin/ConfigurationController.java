package com.mysite.blog.controller.admin;

import com.mysite.blog.service.BlogConfigService;
import com.mysite.blog.uitl.Result;
import com.mysite.blog.uitl.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/14 20:53
 */
@Controller
@RequestMapping("/admin")
public class ConfigurationController {

    @Resource
    private BlogConfigService blogConfigService;

    /**
     * 跳转页
     * @param request request
     * @return 返回页
     */
    @GetMapping("/configurations")
    public String toConfiguration(HttpServletRequest request){
        request.setAttribute("path","configurations");
        request.setAttribute("configurations",blogConfigService.getAllConfigs());
        return "admin/configuration";
    }

    /**
     * 网站信息修改
     * @param websiteName websiteName
     * @param websiteDescription websiteDescription
     * @param websiteLogoUrl websiteLogoUrl
     * @param websiteIconUrl websiteIconUrl
     * @return Result
     */
    @PostMapping("/configurations/website")
    @ResponseBody
    public Result updateWebsiteInfo(@RequestParam(value = "websiteName", required = false) String websiteName,
                                    @RequestParam(value = "websiteDescription", required = false) String websiteDescription,
                                    @RequestParam(value = "websiteHeaderTitle", required = false) String websiteHeaderTitle,
                                    @RequestParam(value = "websiteLogoUrl", required = false) String websiteLogoUrl,
                                    @RequestParam(value = "websiteIconUrl", required = false) String websiteIconUrl,
                                    @RequestParam(value = "websiteCoverUrl", required = false) String websiteCoverUrl){
        int updateResult = 0;
        if (!StringUtils.isEmpty(websiteName)) {
            updateResult += blogConfigService.updateByConfigName("websiteName", websiteName);
        }
        if (!StringUtils.isEmpty(websiteDescription)) {
            updateResult += blogConfigService.updateByConfigName("websiteDescription", websiteDescription);
        }
        if (!StringUtils.isEmpty(websiteHeaderTitle)) {
            updateResult += blogConfigService.updateByConfigName("websiteHeaderTitle", websiteHeaderTitle);
        }
        if (!StringUtils.isEmpty(websiteLogoUrl)) {
            updateResult += blogConfigService.updateByConfigName("websiteLogoUrl", websiteLogoUrl);
        }
        if (!StringUtils.isEmpty(websiteIconUrl)) {
            updateResult += blogConfigService.updateByConfigName("websiteIconUrl", websiteIconUrl);
        }
        if (!StringUtils.isEmpty(websiteCoverUrl)) {
            updateResult += blogConfigService.updateByConfigName("websiteCoverUrl", websiteCoverUrl);
        }
        return ResultGenerator.genSuccessResult(updateResult > 0);
    }

    /**
     * 页脚信息修改
     * @param footerAbout footerAbout
     * @param footerCaseNumber footerCaseNumber
     * @param footerCopyRight footerCopyRight
     * @param footerPoweredBy footerPoweredBy
     * @param footerPoweredByUrl footerPoweredByUrl
     * @return Result
     */
    @PostMapping("/configurations/footer")
    @ResponseBody
    public Result updateWebsiteFooter(@RequestParam(value = "footerAbout", required = false) String footerAbout,
                                      @RequestParam(value = "footerCaseNumber", required = false) String footerCaseNumber,
                                      @RequestParam(value = "footerCopyRight", required = false) String footerCopyRight,
                                      @RequestParam(value = "footerPoweredBy", required = false) String footerPoweredBy,
                                      @RequestParam(value = "footerPoweredByUrl", required = false) String footerPoweredByUrl){
        int updateResult = 0;
        if (!StringUtils.isEmpty(footerAbout)) {
            updateResult += blogConfigService.updateByConfigName("footerAbout", footerAbout);
        }
        if (!StringUtils.isEmpty(footerCaseNumber)) {
            updateResult += blogConfigService.updateByConfigName("footerCaseNumber", footerCaseNumber);
        }
        if (!StringUtils.isEmpty(footerCopyRight)) {
            updateResult += blogConfigService.updateByConfigName("footerCopyRight", footerCopyRight);
        }
        if (!StringUtils.isEmpty(footerPoweredBy)) {
            updateResult += blogConfigService.updateByConfigName("footerPoweredBy", footerPoweredBy);
        }
        if (!StringUtils.isEmpty(footerPoweredByUrl)) {
            updateResult += blogConfigService.updateByConfigName("footerPoweredByUrl", footerPoweredByUrl);
        }
        return ResultGenerator.genSuccessResult(updateResult > 0);
    }
}
