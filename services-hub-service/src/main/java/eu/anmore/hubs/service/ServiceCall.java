package eu.anmore.hubs.service;

import java.util.Collection;
import java.util.Date;

public class ServiceCall {

    private ServiceRequest serviceRequest;
    private Date requestDate;
    private ServiceResponse serviceResponse;
    private Date responseDate;
    private Long processingTime;
    private Exception exception;
    private Date exceptionDate;
    private ServiceEndpoint endpoint;
    private Collection<ServiceEndpoint> endpointMatches;
    private String requestUrl;
    private String exceptionMessageDetails;

    public ServiceCall() {
    }

    public void setServiceRequest(ServiceRequest serviceRequest, Date date) {
        this.serviceRequest = serviceRequest;
        this.requestDate = date;
    }

    public void setServiceResponse(ServiceResponse serviceResponse, Date responseDate) {
        this.serviceResponse = serviceResponse;
        this.responseDate = responseDate;
        this.processingTime = responseDate.getTime() - requestDate.getTime();
    }

    public void setException(Exception exception, Date date) {
        this.exception = exception;
        this.exceptionDate = date;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setSelectedEndpoint(ServiceEndpoint selectedEndpoint) {
        this.endpoint = selectedEndpoint;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setExceptionMessageDetails(String exceptionMessageDetails) {
        this.exceptionMessageDetails = exceptionMessageDetails;
    }

    public void setEndpointMatches(Collection<ServiceEndpoint> endpointMatches) {
        this.endpointMatches = endpointMatches;
    }

    public ServiceEndpoint getEndpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServiceCall{");
        sb.append("serviceRequest=").append(serviceRequest);
        sb.append(", requestDate=").append(requestDate);
        sb.append(", serviceResponse=").append(serviceResponse);
        sb.append(", responseDate=").append(responseDate);
        sb.append(", processingTime=").append(processingTime);
        sb.append(", exception=").append(exception);
        sb.append(", exceptionDate=").append(exceptionDate);
        sb.append(", endpoint=").append(endpoint);
        sb.append(", endpointMatches=").append(endpointMatches);
        sb.append(", requestUrl='").append(requestUrl).append('\'');
        sb.append(", exceptionMessageDetails='").append(exceptionMessageDetails).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
