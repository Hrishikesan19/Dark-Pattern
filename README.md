# 🕵️‍♂️ Dark Pattern Detector

A real-time **Dark Pattern Detector** that combines **web scraping using Jsoup** and **zero-shot classification using a machine learning model**. This tool analyzes elements from a given website and identifies possible dark patterns in the user interface text.

---

## 📌 What Are Dark Patterns?

Dark patterns are deceptive design tactics used in websites and apps to trick users into doing things they might not want to — such as subscribing to something, making a purchase, or revealing personal information. Common types include:

- False Urgency  
- Confirm Shaming  
- Forced Action  
- Bait and Switch  
- Drip Pricing  
- Interface Interference  
- Basket Sneaking  
- Subscription Traps  
- Disguised Advertisements  
- Nagging  

---

## 🎯 Project Objective

This project was created to apply **web scraping (using Jsoup)** in a practical way by detecting dark patterns from real websites.

You don't need to be a Machine Learning expert to understand this! The heavy lifting is handled by a pre-trained model from HuggingFace (`facebook/bart-large-mnli`) through FastAPI.

---

## 🧩 How It Works

### 🔧 Components

1. **Java Scraper (Jsoup)**  
   - Prompts the user for a URL  
   - Scrapes all relevant UI elements (buttons, links, labels, etc.)  
   - Sends each text to the Python API for classification  

2. **Python API (FastAPI + Transformers)**  
   - Receives text via a POST request  
   - Uses a zero-shot classification model to detect the most likely dark pattern  
   - Returns the label and confidence score  

---

## 🚀 Getting Started

### 1. Clone the Repo

```bash
git clone https://github.com/yourusername/dark-pattern-detector.git
cd dark-pattern-detector
