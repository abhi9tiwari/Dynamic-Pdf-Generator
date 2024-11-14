# Dynamic PDF Generator

A Spring Boot application that generates PDFs dynamically based on JSON data provided via a REST API. It uses Thymeleaf as a template engine to create the PDF structure and saves each PDF with a unique ID, allowing for easy retrieval and download.

## Features
- Generates a PDF file from a JSON request body
- Stores the PDF on the server's local file system for future retrieval
- Allows PDF download by a unique identifier (ID)

## Technologies Used
- **Java**: Core language
- **Spring Boot**: Framework for building the application
- **Thymeleaf**: Template engine for generating HTML used in PDF
- **openhtmltopdf**: Library to convert HTML into a PDF document

## Setup and Configuration

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd dynamicpdfgenerator

2. Add pdf-storage Directory:
    Ensure there is a pdf-storage folder in the project root or it will be created automatically when the application first runs.

3. Configure the Storage Path: pdf-storage
	
4. Dependencies:
	Make sure to add all the required dependencies present in pom.xml

Api EndPoints : 

1. Generate PDF

    Endpoint: POST /api/pdf/generate (http://localhost:8080/api/pdf/generate)
    Description: Generates and stores a PDF based on the provided JSON data.
    Request Body Example:
	{
    "seller": "XYZ Pvt. Ltd.",
    "sellerGstin": "29AABBCCDD121ZD",
    "sellerAddress": "New Delhi, India",
    "buyer": "Vedant Computers",
    "buyerGstin": "29AABBCCDD131ZD",
    "buyerAddress": "New Delhi, India",
    "items": [
        {
            "name": "Product 1",
            "quantity": "12 Nos",
            "rate": 123.00,
            "amount": 1476.00
        }
    ]
}

2. Download PDF

    Endpoint: GET /api/pdf/download/{id} (http://localhost:8080/api/pdf/download/1830121758)
    Description: Downloads the stored PDF corresponding to the unique ID.
    Path Variable: {id} is the unique identifier for the PDF.
    Response: Returns the PDF file for download.
	
Important Notes
    PDF Storage: PDFs are stored in the pdf-storage folder in the root directory.
    File Naming: Each PDF is named based on a SHA-256 hash of the request data.