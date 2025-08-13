# Racist Cars — Measuring & Mitigating Bias in Car-Centric Computer Vision

> **Goal:** Evaluate whether common computer-vision pipelines (e.g., pedestrian detection around cars, license-plate/driver detection, car color/model classification) behave differently across demographic or environmental groups, and provide tools to diagnose and reduce that bias.

---

## ✨ What’s inside

- **Metrics for bias**: group-wise performance (precision/recall, FPR/FNR), calibration, subgroup AUC
- **Fairness reports**: auto-generated markdown/CSV summaries and plots
- **Mitigations**: reweighting, threshold optimization, post-processing (equalized odds), data balancing
- **Reproducible experiments**: scripts to train/evaluate baselines and fairness-aware variants

---

## 🔎 Why this matters

Perception systems used around vehicles (ADAS, AV, traffic cameras, parking enforcement) can under-perform for certain groups due to dataset imbalance, lighting, or model bias. This repo makes it easy to **measure** those gaps and **prototype fixes**.  
We use the term “racist cars” to critique biased systems—not to endorse the phrasing. See the [Ethics](#-ethics--language-note) section.

---

## 🗂 Project structure


> If you imported this as an IntelliJ IDEA project (`*.iml` present), code likely lives under `src/`. Adjust paths above as needed.

---

## 🚀 Getting started

### 1) Prerequisites

- **Option A (Java/Kotlin stack)**  
  - JDK 17+  
  - Gradle or Maven  
  - (Optional) OpenCV/DeepLearning frameworks via JNI bindings

- **Option B (Python stack)**  
  - Python 3.10+  
  - `pip install -r requirements.txt`  🔧 create this file if using Python  
  - Common libs: `numpy`, `pandas`, `scikit-learn`, `torch`/`tensorflow`, `matplotlib`

### 2) Setup

```bash
# clone
git clone https://github.com/OaaMatin/racist-cars
cd racist-cars

# Java (Gradle) 🔧
./gradlew build

# OR Python 🔧
python -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
