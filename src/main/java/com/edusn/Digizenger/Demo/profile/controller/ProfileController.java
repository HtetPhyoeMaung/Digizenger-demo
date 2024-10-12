package com.edusn.Digizenger.Demo.profile.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.profile.service.*;
import com.edusn.Digizenger.Demo.profile.service.about.*;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/profile/")
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileImageService profileImageService;
    private final CoverImageService coverImageService;
    private final BioProfileService bioProfileService;
    private final UsernameService usernameService;
    private final ProfileCareerService profileCareerService;
    private final AboutCareerHistoryService careerHistoryService;
    private final AboutProvidedService aboutProvidedService;
    private final FollowerService followerService;
    private final FollowingService followingService;
    private final NeighborService neighborService;
    private final AboutEducationHistoryService educationHistoryService;
    private final SchoolService schoolService;
    private final CompanyService companyService;

    @GetMapping("/test")
    public String test(){
        return "successfully test";
    }

    @GetMapping("/")
    public ResponseEntity<Response> getProfile(HttpServletRequest request) throws IOException {
        return profileService.showUserProfile(request);
    }


    @GetMapping("/{username}")
    public ResponseEntity<Response> getProfileByUrl(@PathVariable("username") String username,
                                                    HttpServletRequest request) throws IOException {
        return profileService.getProfileByProfileUrlLink(username,request);
    }

    /** Profile Image **/

    @PostMapping("/p-image")
    public ResponseEntity<Response> uploadProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return profileImageService.uploadProfileImage(file, request);
    }

    @DeleteMapping("/p-image")
    public ResponseEntity<Response> deleteProfileImage(HttpServletRequest request) throws FileNotFoundException {
        return profileImageService.deleteProfileImage(request);
    }

    @PutMapping("/p-image")
    public ResponseEntity<Response> updateProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return  profileImageService.updateProfileImage(file, request);
    }

    /** Cover Image **/

    @PostMapping("/c-image")
    public ResponseEntity<Response> uploadCoverImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return coverImageService.uploadCoverImage(file, request);
    }

    @DeleteMapping("/c-image")
    public ResponseEntity<Response> deleteCoverImage(HttpServletRequest request) throws FileNotFoundException {
        return coverImageService.deleteCoverImage(request);
    }

    @PutMapping("/c-image")
    public ResponseEntity<Response> updateCoverImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return coverImageService.updateCoverImage(file, request);
    }

    /** Bio **/

    @PostMapping("/bio")
    public ResponseEntity<Response> uploadBio(@RequestParam("bio") String bio, HttpServletRequest request){
        return bioProfileService.uploadBio(bio, request);
    }

    @DeleteMapping("/bio")
    public ResponseEntity<Response> removeBio(HttpServletRequest request){
        return bioProfileService.removeBio(request);
    }

    /** Username **/

    @PostMapping("/username")
    public ResponseEntity<Response> uploadUsername(@RequestParam("username") String username, HttpServletRequest request){
        return usernameService.uploadUsername(username, request);
    }

    @DeleteMapping("/username")
    public ResponseEntity<Response> removeUsername(HttpServletRequest request){
        return usernameService.removeUsername(request);
    }

    /** Profile Career **/

    @PostMapping("/career")
    public ResponseEntity<Response> uploadCareer(@RequestParam("profileCareer") String profileCareer, HttpServletRequest request){
        return profileCareerService.uploadCareer(profileCareer, request);
    }

    @DeleteMapping("/career")
    public ResponseEntity<Response> removeCareer(HttpServletRequest request){
        return profileCareerService.removeCareer(request);
    }


    /** Service Provided **/

    @GetMapping("/service-provided/{serviceName}")
    public ResponseEntity<Response> findByServiceName(HttpServletRequest request,
                                                      @PathVariable("serviceName") String serviceName){
        return aboutProvidedService.findByServiceName(request ,serviceName);
    }

    @PostMapping("/service-provided/{id}")
    public ResponseEntity<Response> uploadServiceProvidedById(HttpServletRequest request,
                                                              @PathVariable("id") Long id){
        return aboutProvidedService.uploadServiceProvidedById(request, id);
    }

    @PostMapping("/service-provided")
    public ResponseEntity<Response> uploadServiceProvided(HttpServletRequest request,
                                                          @RequestParam("service") String service){
        return aboutProvidedService.uploadServiceProvided(request, service);
    }

    @PutMapping("/service-provided")
    public ResponseEntity<Response> updateServiceProvided(HttpServletRequest request,
                                                          @RequestParam("id") Long id,
                                                          @RequestParam("service") String service){
        return aboutProvidedService.updateServiceProvided(request, id, service);
    }

    @DeleteMapping("/service-provided/{id}")
    public ResponseEntity<Response> removeServiceProvided(HttpServletRequest request,
                                                          @PathVariable("id") Long id){
        return aboutProvidedService.removeServiceProvided(request, id);
    }
    /** Follower **/
    @GetMapping("/followers")
    public ResponseEntity<Response> profileFollowers(@RequestParam("_page") int _page,
                                                     @RequestParam("_limit") int _limit,
                                                     @RequestParam("profileUrl") String profileUrl,
                                                     HttpServletRequest request){
        return followerService.getProfileFollowersByPage(_page, _limit, profileUrl, request);
    }

    @PostMapping("/followers")
    public ResponseEntity<Response> followUserProfile(HttpServletRequest request,
                                                      @RequestParam("toFollowUserProfileUrl") String url){
        return followerService.followToProfile(request, url);
    }

    @DeleteMapping("/followers")
    public ResponseEntity<Response> toUnFollowUserProfile(HttpServletRequest request,
                                                          @RequestParam("toUnfollowUserProfileUrl") String url){
        return followerService.unFollowToProfile(request, url);
    }

    /** Following **/
    @GetMapping("/following")
    public ResponseEntity<Response> profileFollowing(@RequestParam("_page") int _page,
                                                     @RequestParam("_limit") int _limit,
                                                     @RequestParam("profileUrl") String profileUrl,
                                                     HttpServletRequest request){
        return followingService.getProfileFollowingByPage(_page, _limit, profileUrl, request);
    }

    /** Neighbor **/
    @GetMapping("/neighbors")
    public ResponseEntity<Response> profileNeighbors(@RequestParam("_page") int _page,
                                                     @RequestParam("_limit") int _limit,
                                                     @RequestParam("profileUrl") String profileUrl,
                                                     HttpServletRequest request){
        return neighborService.getProfileNeighborsByPage(_page, _limit, profileUrl, request);
    }

    /** Education History **/
    @PostMapping("/education-history")
    public ResponseEntity<Response> uploadEducationHistory(HttpServletRequest request,
                                                           @RequestParam("schoolName") String schoolName,
                                                           @Nullable @RequestParam(value = "logoImage",required = false) MultipartFile logoImage,
                                                           @Nullable @RequestParam(value = "degreeName",required = false) String degreeName,
                                                           @RequestParam("fieldOfStudy") String fieldOfStudy,
                                                           @RequestParam("joinDate") LocalDate joinDate,
                                                           @Nullable @RequestParam(value = "endDate",required = false) LocalDate endDate) throws IOException {
        return educationHistoryService.uploadEducationHistory(request, schoolName, logoImage, degreeName, fieldOfStudy, joinDate, endDate);
    }

    @PutMapping("/education-history/{id}")
    public ResponseEntity<Response> updateEducationHistory(HttpServletRequest request,
                                                           @PathVariable("id") Long educationHistoryId,
                                                           @Nullable @RequestParam("schoolName") String schoolName,
                                                           @Nullable @RequestParam(value = "degreeName", required = false) String degreeName,
                                                           @Nullable @RequestParam(value = "fieldOfStudy", required = false) String fieldOfStudy,
                                                           @Nullable @RequestParam(value = "logoImage", required = false) MultipartFile logoImage,
                                                           @Nullable @RequestParam(value = "joinDate", required = false) LocalDate joinDate,
                                                           @Nullable @RequestParam(value = "endDate", required = false) LocalDate endDate) throws IOException {
        return educationHistoryService.updateEducationHistory(request, educationHistoryId, schoolName, degreeName, fieldOfStudy, logoImage, joinDate, endDate);
    }

    @DeleteMapping("/education-history/{id}")
    public ResponseEntity<Response> deleteEducationHistory(HttpServletRequest request,
                                                           @PathVariable("id") Long id){
        return educationHistoryService.removeEducationHistory(request, id);
    }

    @GetMapping("/school/{schoolName}")
    public ResponseEntity<Response> findSchoolByDynamicName(HttpServletRequest request,
                                                            @PathVariable("schoolName") String name){
        return schoolService.getExistingSchoolNameByName(request, name);
    }


    /** Career History **/
    @PostMapping("/career-history")
    public ResponseEntity<Response> uploadCareerHistory(HttpServletRequest request,
                                                           @RequestParam("companyName") String companyNmae,
                                                           @Nullable @RequestParam(value = "logoImage",required = false) MultipartFile logoImage,
                                                           @Nullable @RequestParam(value = "designation",required = false) String designation,
                                                           @RequestParam("joinDate") LocalDate joinDate,
                                                           @Nullable @RequestParam(value = "endDate",required = false) LocalDate endDate) throws IOException {
        return careerHistoryService.uploadCareerHistory(request, companyNmae, logoImage, designation, joinDate, endDate);
    }

    @PutMapping("/career-history/{id}")
    public ResponseEntity<Response> updateCareerHistory(HttpServletRequest request,
                                                           @PathVariable("id") Long careerHistoryId,
                                                           @Nullable @RequestParam("companyName") String companyName,
                                                           @Nullable @RequestParam(value = "designation", required = false) String designation,
                                                           @Nullable @RequestParam(value = "logoImage", required = false) MultipartFile logoImage,
                                                           @Nullable @RequestParam(value = "joinDate", required = false) LocalDate joinDate,
                                                           @Nullable @RequestParam(value = "endDate", required = false) LocalDate endDate) throws IOException {
        return careerHistoryService.updateCareerHistory(request, careerHistoryId, companyName, designation, logoImage, joinDate, endDate);
    }

    @DeleteMapping("/career-history/{id}")
    public ResponseEntity<Response> deleteCareerHistory(HttpServletRequest request,
                                                           @PathVariable("id") Long id){
        return careerHistoryService.removeCareerHistory(request, id);
    }

    @GetMapping("/company/{companyName}")
    public ResponseEntity<Response> findCompanyByDynamicName(HttpServletRequest request,
                                                            @PathVariable("companyName") String name){
        return companyService.getExistingCompanyNameByName(request, name);
    }





}
