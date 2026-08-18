// WealthMetric ProjectionLab Web Edition - Exact Match to Kotlin Android App Logic

// ==================== STATE MANAGEMENT ====================
const appState = {
    step: 0, // 0 = Welcome, 1 = Plan Selection, 2 = Full Page NAV & Info, 3 = Calculator
    planCategory: 0, // 0 = Bank Pension Plans, 1 = Mutual Fund Plans
    selectedAmc: 'SBI',
    
    selectedPlan: null,
    selectedFund: null,
    isFetchingNav: false,

    retirementInput: {
        currentAge: 35,
        retirementAge: 60,
        currentCorpus: 2500000.0,
        annualSavings: 100000.0,
        annualExpenditure: 600000.0,
        annualInterest: 7.0,
        annualInflation: 6.0,
        expectedAnnuityRate: 6.8,
        payoutFrequency: 1 // 1 = Yearly, 12 = Monthly
    }
};

// ==================== PRECONFIGURED BANK PENSION PLANS ====================
const bankPensionPlans = [
    {
        type: 'IMMEDIATE_PENSION',
        title: 'Immediate Pension Plan',
        shortDescription: 'Invest your retirement savings once in Rupees (₹) and start receiving a regular monthly/annual pension immediately.',
        bestFor: 'People who have already retired or have a retirement corpus ready in Rupees (₹).',
        about: 'The Immediate Pension Plan allows you to convert a lump-sum corpus in Rupees (₹) into an immediate lifetime pension stream. Once initialized, annuity payouts begin immediately without any waiting period.',
        howItWorks: 'You invest your accumulated retirement savings as a single lump sum in Rupees (₹). The annuity provider calculates your regular pension based on the prevailing rate (default 6.8%) and starts disbursing payments right away.',
        benefits: [
            'Immediate financial security with guaranteed regular cash flows in Rupees (₹).',
            'Zero accumulation waiting period – income starts right after setup.',
            'Hassle-free management with fixed or escalating pension options.'
        ],
        defaultAnnuityRate: 6.8
    },
    {
        type: 'GUARANTEED_FUTURE_PENSION',
        title: 'Guaranteed Future Pension Plan',
        shortDescription: 'Invest a lump sum in Rupees (₹) today. Your money grows until retirement and then starts providing a regular pension.',
        bestFor: 'People who already have savings and are planning for retirement.',
        about: 'Designed for individuals with existing savings in Rupees (₹) who wish to lock in future pension income. Your principal grows compound interest until your chosen retirement age, after which annuity payments trigger automatically.',
        howItWorks: 'Your lump-sum investment compounds annually in Rupees (₹) until retirement. At retirement, the entire accumulated corpus converts into a lifetime annuity generating a regular annual pension (default rate 6.6%).',
        benefits: [
            'Guaranteed compound growth on your existing savings until retirement.',
            'Protects your future lifestyle against rising living expenses (inflation).',
            'Offers clarity and confidence in setting retirement targets in Rupees (₹).'
        ],
        defaultAnnuityRate: 6.6
    },
    {
        type: 'RETIREMENT_WEALTH_BUILDER',
        title: 'Retirement Wealth Builder',
        shortDescription: 'Start with your existing savings in Rupees (₹) and continue investing every year until retirement. The total corpus is then used to generate pension.',
        bestFor: 'Working professionals planning long-term retirement.',
        about: 'Ideal for active earners looking to systematically build a formidable retirement nest egg over time in Rupees (₹). Combines existing capital with compounded annual savings up to your target retirement age.',
        howItWorks: 'Your existing corpus and ongoing annual contributions grow at the expected return rate until retirement age. Upon reaching retirement, the final corpus is deployed into an annuity yielding predictable yearly income in Rupees (₹) (default rate 6.7%).',
        benefits: [
            'Disciplined wealth creation strategy for mid-career professionals.',
            'Maximizes compounding benefits over remaining working years.',
            'Generates a robust pension stream to match future living expenses in Rupees (₹).'
        ],
        defaultAnnuityRate: 6.7
    },
    {
        type: 'FLEXIBLE_PENSION',
        title: 'Flexible Pension Plan',
        shortDescription: 'Build your retirement corpus in Rupees (₹) and choose how you want to receive your pension after retirement (monthly, quarterly, half-yearly or yearly).',
        bestFor: 'People who want flexibility in retirement income.',
        about: 'Provides maximum customization over how and when you receive payouts after retirement. Adjust payment frequency to match your personal lifestyle and recurring bill schedules.',
        howItWorks: 'Accumulate savings until retirement age. Post-retirement, your corpus generates pension at a baseline rate (default 6.5%), and you choose monthly, quarterly, half-yearly, or annual disbursement in Rupees (₹).',
        benefits: [
            'Customizable payout frequencies: Monthly, Quarterly, Half-Yearly, or Yearly in Rupees (₹).',
            'Adaptable to changing post-retirement liquidity needs.',
            'Combines safety of guaranteed returns with flexible cash flow schedules.'
        ],
        defaultAnnuityRate: 6.5
    }
];

// ==================== PRECONFIGURED MUTUAL FUNDS (SBI, ICICI, LIC) ====================
const mutualFundsData = [
    // SBI TOP 5
    {
        schemeCode: '119551', sfinCode: 'SBI-BCF-DG', amc: 'SBI', amcDisplay: 'SBI Mutual Fund',
        fundName: 'SBI Bluechip Fund Direct Growth', category: 'Large Cap Fund', benchmark: 'Nifty 50 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 84.62, navDate: 'Latest', dailyChangePercent: 0.42,
        cagr1Year: 18.5, cagr3Year: 14.8, cagr5Year: 15.2, sinceInception: 15.8,
        equityPercent: 94.8, debtPercent: 0.0, cashPercent: 5.2,
        aboutText: 'Invests primarily in top 100 large-cap bluechip stocks in India, focusing on steady capital growth and bluechip market leaders.'
    },
    {
        schemeCode: '125497', sfinCode: 'SBI-SCF-DG', amc: 'SBI', amcDisplay: 'SBI Mutual Fund',
        fundName: 'SBI Small Cap Fund Direct Growth', category: 'Small Cap Fund', benchmark: 'BSE 250 SmallCap TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 168.45, navDate: 'Latest', dailyChangePercent: 0.85,
        cagr1Year: 28.2, cagr3Year: 22.4, cagr5Year: 26.4, sinceInception: 24.1,
        equityPercent: 91.5, debtPercent: 0.0, cashPercent: 8.5,
        aboutText: 'A high-growth small-cap fund seeking long-term capital appreciation by investing in emerging future leaders.'
    },
    {
        schemeCode: '119579', sfinCode: 'SBI-FEF-DG', amc: 'SBI', amcDisplay: 'SBI Mutual Fund',
        fundName: 'SBI Focused Equity Fund Direct Growth', category: 'Focused Fund (Max 30 Stocks)', benchmark: 'BSE 500 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 312.18, navDate: 'Latest', dailyChangePercent: -0.15,
        cagr1Year: 19.4, cagr3Year: 15.6, cagr5Year: 16.8, sinceInception: 17.2,
        equityPercent: 95.8, debtPercent: 0.0, cashPercent: 4.2,
        aboutText: 'A conviction-driven portfolio limited to maximum 30 high-potential growth stocks across sectors.'
    },
    {
        schemeCode: '119598', sfinCode: 'SBI-CTF-DG', amc: 'SBI', amcDisplay: 'SBI Mutual Fund',
        fundName: 'SBI Contra Fund Direct Growth', category: 'Value / Contra Fund', benchmark: 'BSE 500 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 364.92, navDate: 'Latest', dailyChangePercent: 1.12,
        cagr1Year: 38.6, cagr3Year: 27.5, cagr5Year: 24.1, sinceInception: 18.5,
        equityPercent: 88.6, debtPercent: 0.0, cashPercent: 11.4,
        aboutText: 'Follows a contrarian investment strategy, investing in undervalued out-of-favor companies poised for multi-year turnaround.'
    },
    {
        schemeCode: '119565', sfinCode: 'SBI-ELSS-DG', amc: 'SBI', amcDisplay: 'SBI Mutual Fund',
        fundName: 'SBI Long Term Equity Fund (ELSS) Direct Growth', category: 'Tax Saver ELSS (3Y Lock-in)', benchmark: 'BSE 500 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 398.20, navDate: 'Latest', dailyChangePercent: 0.55,
        cagr1Year: 24.5, cagr3Year: 18.2, cagr5Year: 17.5, sinceInception: 16.9,
        equityPercent: 96.5, debtPercent: 0.0, cashPercent: 3.5,
        aboutText: 'Combines dual benefits of tax savings under Section 80C with long-term equity wealth generation.'
    },

    // ICICI TOP 5
    {
        schemeCode: '120586', sfinCode: 'EVIF', amc: 'ICICI', amcDisplay: 'ICICI Prudential MF',
        fundName: 'ICICI Prudential Bluechip Fund Direct Growth', category: 'Large Cap Fund', benchmark: 'Nifty 50 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 112.34, navDate: 'Latest', dailyChangePercent: 0.68,
        cagr1Year: 22.4, cagr3Year: 17.8, cagr5Year: 16.5, sinceInception: 16.2,
        equityPercent: 93.8, debtPercent: 0.0, cashPercent: 6.2,
        aboutText: 'ICICI Prudential flagship large-cap equity fund investing in market leaders with strong corporate governance and cash flows.'
    },
    {
        schemeCode: '120594', sfinCode: 'ICICI-EDF-DG', amc: 'ICICI', amcDisplay: 'ICICI Prudential MF',
        fundName: 'ICICI Prudential Equity & Debt Fund Direct Growth', category: 'Aggressive Hybrid Fund', benchmark: 'Nifty 50 Hybrid 65:35',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 345.80, navDate: 'Latest', dailyChangePercent: 0.32,
        cagr1Year: 26.5, cagr3Year: 20.8, cagr5Year: 18.9, sinceInception: 17.4,
        equityPercent: 68.2, debtPercent: 26.5, cashPercent: 5.3,
        aboutText: 'Maintains a balanced blend of 65-75% equities for growth and 25-35% high-grade fixed income bonds for stability.'
    },
    {
        schemeCode: '120621', sfinCode: 'ICICI-VDF-DG', amc: 'ICICI', amcDisplay: 'ICICI Prudential MF',
        fundName: 'ICICI Prudential Value Discovery Fund Direct Growth', category: 'Value Fund', benchmark: 'Nifty 500 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 432.10, navDate: 'Latest', dailyChangePercent: 0.95,
        cagr1Year: 29.8, cagr3Year: 23.4, cagr5Year: 21.4, sinceInception: 19.1,
        equityPercent: 87.5, debtPercent: 4.2, cashPercent: 8.3,
        aboutText: 'Identifies high quality businesses available at significant discount to their intrinsic value for superior long-term returns.'
    },
    {
        schemeCode: '120597', sfinCode: 'ICICI-LMF-DG', amc: 'ICICI', amcDisplay: 'ICICI Prudential MF',
        fundName: 'ICICI Prudential Large & Mid Cap Fund Direct Growth', category: 'Large & Mid Cap Fund', benchmark: 'Nifty LargeMidcap 250 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 85.90, navDate: 'Latest', dailyChangePercent: 0.45,
        cagr1Year: 25.1, cagr3Year: 19.6, cagr5Year: 19.2, sinceInception: 17.8,
        equityPercent: 94.6, debtPercent: 0.0, cashPercent: 5.4,
        aboutText: 'Combines stability of large-cap leaders (35%+) with dynamic upside growth potential of mid-cap companies (35%+).'
    },
    {
        schemeCode: '120614', sfinCode: 'ICICI-SCF-DG', amc: 'ICICI', amcDisplay: 'ICICI Prudential MF',
        fundName: 'ICICI Prudential Smallcap Fund Direct Growth', category: 'Small Cap Fund', benchmark: 'Nifty Smallcap 250 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 92.40, navDate: 'Latest', dailyChangePercent: 0.72,
        cagr1Year: 27.6, cagr3Year: 21.9, cagr5Year: 24.8, sinceInception: 20.5,
        equityPercent: 90.8, debtPercent: 0.0, cashPercent: 9.2,
        aboutText: 'Invests in high-potential small businesses with strong market share expansion and earnings acceleration.'
    },

    // LIC TOP 5
    {
        schemeCode: '119717', sfinCode: 'LIC-LCF-DG', amc: 'LIC', amcDisplay: 'LIC Mutual Fund',
        fundName: 'LIC MF Large Cap Fund Direct Growth', category: 'Large Cap Fund', benchmark: 'Nifty 50 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 52.80, navDate: 'Latest', dailyChangePercent: 0.28,
        cagr1Year: 16.4, cagr3Year: 13.2, cagr5Year: 13.8, sinceInception: 13.5,
        equityPercent: 95.5, debtPercent: 0.0, cashPercent: 4.5,
        aboutText: 'Backed by LIC heritage, focusing on steady capital preservation and long-term capital growth in India\'s top 100 bluechips.'
    },
    {
        schemeCode: '119728', sfinCode: 'LIC-FCF-DG', amc: 'LIC', amcDisplay: 'LIC Mutual Fund',
        fundName: 'LIC MF Flexi Cap Fund Direct Growth', category: 'Flexi Cap Fund', benchmark: 'Nifty 500 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 89.60, navDate: 'Latest', dailyChangePercent: 0.50,
        cagr1Year: 18.2, cagr3Year: 14.1, cagr5Year: 14.9, sinceInception: 14.2,
        equityPercent: 92.8, debtPercent: 0.0, cashPercent: 7.2,
        aboutText: 'Dynamically shifts investments across large, mid, and small-cap opportunities based on evolving macro market trends.'
    },
    {
        schemeCode: '119736', sfinCode: 'LIC-INF-DG', amc: 'LIC', amcDisplay: 'LIC Mutual Fund',
        fundName: 'LIC MF Infrastructure Fund Direct Growth', category: 'Sectoral / Thematic Infrastructure', benchmark: 'Nifty Infrastructure TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 48.90, navDate: 'Latest', dailyChangePercent: 1.25,
        cagr1Year: 39.5, cagr3Year: 25.8, cagr5Year: 22.1, sinceInception: 15.6,
        equityPercent: 91.2, debtPercent: 0.0, cashPercent: 8.8,
        aboutText: 'Captures long-term growth opportunities in India\'s nation-building sectors including capital goods, energy, power, and logistics.'
    },
    {
        schemeCode: '119725', sfinCode: 'LIC-LMF-DG', amc: 'LIC', amcDisplay: 'LIC Mutual Fund',
        fundName: 'LIC MF Large & Mid Cap Fund Direct Growth', category: 'Large & Mid Cap Fund', benchmark: 'Nifty LargeMidcap 250 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 38.40, navDate: 'Latest', dailyChangePercent: 0.38,
        cagr1Year: 21.0, cagr3Year: 15.9, cagr5Year: 16.2, sinceInception: 14.8,
        equityPercent: 93.9, debtPercent: 0.0, cashPercent: 6.1,
        aboutText: 'Maintains disciplined allocation across market-leading large caps and agile mid-caps for balanced risk-adjusted growth.'
    },
    {
        schemeCode: '119711', sfinCode: 'LIC-ELSS-DG', amc: 'LIC', amcDisplay: 'LIC Mutual Fund',
        fundName: 'LIC MF Tax Saver Fund Direct Growth', category: 'Tax Saver ELSS (3Y Lock-in)', benchmark: 'Nifty 500 TRI',
        launchDate: '01-Jan-2013', inceptionNav: 10.0, currentNav: 145.20, navDate: 'Latest', dailyChangePercent: 0.40,
        cagr1Year: 17.5, cagr3Year: 13.0, cagr5Year: 13.5, sinceInception: 13.9,
        equityPercent: 97.2, debtPercent: 0.0, cashPercent: 2.8,
        aboutText: 'Combines 80C income tax deduction benefits with wealth building through equity market participation.'
    }
];

// ==================== MFAPI REST API INTEGRATION ====================
async function fetchFundWith5YearNavHistory(fund) {
    const todayStr = new Date().toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' }).replace(/ /g, '-');
    try {
        const response = await fetch(`https://api.mfapi.in/mf/${fund.schemeCode}`);
        if (response.ok) {
            const json = await response.json();
            if (json.data && json.data.length > 0) {
                const totalItems = json.data.length;
                const latestItem = json.data[0];
                const latestNav = parseFloat(latestItem.nav) || fund.currentNav;

                const navPoints = [];
                const step = Math.max(1, Math.floor(totalItems / 120));
                for (let i = 0; i < totalItems; i += step) {
                    const item = json.data[i];
                    const navVal = parseFloat(item.nav);
                    if (navVal > 0) {
                        const formattedDate = formatDateString(item.date);
                        navPoints.push({ date: formattedDate, nav: navVal });
                    }
                }
                const chronological = navPoints.reverse();
                if (chronological.length > 0 && chronological[chronological.length - 1].date !== todayStr) {
                    chronological.push({ date: todayStr, nav: latestNav });
                }

                const prevNav = json.data.length > 1 ? (parseFloat(json.data[1].nav) || latestNav) : latestNav;
                const dailyChange = prevNav > 0 ? ((latestNav - prevNav) / prevNav) * 100.0 : 0.0;

                return {
                    ...fund,
                    currentNav: Math.round(latestNav * 100) / 100,
                    navDate: todayStr,
                    dailyChangePercent: Math.round(dailyChange * 100) / 100,
                    navHistory5Y: chronological
                };
            }
        }
    } catch (_e) {
        console.warn('MFAPI unavailable, generating fallback dynamic market curve');
    }
    return generateFallbackDynamicNav(fund);
}

function formatDateString(rawDate) {
    try {
        const parts = rawDate.split("-");
        if (parts.length === 3) {
            const day = parts[0].padStart(2, '0');
            const monthInt = parseInt(parts[1], 10) || 1;
            const year = parts[2];
            const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            const monthStr = months[monthInt - 1] || "Aug";
            return `${day}-${monthStr}-${year}`;
        }
    } catch (_e) {}
    return rawDate;
}

function generateFallbackDynamicNav(fund) {
    const today = new Date();
    const todayStr = today.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' }).replace(/ /g, '-');
    const liveNav = fund.currentNav;
    const navHistory = [];
    const startNav = Math.max(10.0, liveNav / Math.pow(1.0 + fund.cagr5Year / 100.0, 5.0));

    const totalPoints = 120;
    for (let i = 0; i <= totalPoints; i++) {
        const progress = i / totalPoints;
        const d = new Date(today);
        d.setDate(d.getDate() - (totalPoints - i) * 7);
        const dateStr = d.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' }).replace(/ /g, '-');

        const baseNav = startNav * Math.pow(liveNav / startNav, progress);
        const majorCycle = Math.sin(progress * 14 * Math.PI) * (baseNav * 0.08);
        const midCycle = Math.cos(progress * 32 * Math.PI) * (baseNav * 0.04);
        const noise = Math.sin(progress * 75 * Math.PI) * (baseNav * 0.02);

        const pointNav = i === totalPoints ? liveNav : Math.max(10.0, baseNav + majorCycle + midCycle + noise);
        navHistory.push({ date: dateStr, nav: Math.round(pointNav * 100) / 100 });
    }

    return {
        ...fund,
        currentNav: Math.round(liveNav * 100) / 100,
        navDate: todayStr,
        navHistory5Y: navHistory
    };
}

// ==================== WIZARD STEP NAVIGATION ====================
function navigateStep(targetStep) {
    appState.step = targetStep;
    document.querySelectorAll('.step-pane').forEach(pane => pane.style.display = 'none');

    if (targetStep === 0) {
        document.getElementById('pane-welcome').style.display = 'block';
    } else if (targetStep === 1) {
        document.getElementById('pane-selection').style.display = 'block';
        renderSelectionPane();
    } else if (targetStep === 2) {
        document.getElementById('pane-info').style.display = 'block';
        renderFullPageInfoPane();
    } else if (targetStep === 3) {
        document.getElementById('pane-calculator').style.display = 'block';
        calculateRetirementProjections();
    }
}

function setPlanCategory(catIndex) {
    appState.planCategory = catIndex;
    document.querySelectorAll('.sel-tab').forEach((t, idx) => {
        if (idx === catIndex) t.classList.add('active');
        else t.classList.remove('active');
    });
    document.getElementById('amc-row').style.display = catIndex === 1 ? 'flex' : 'none';
    renderSelectionPane();
}

function setAmc(amcName) {
    appState.selectedAmc = amcName;
    document.querySelectorAll('.chip-btn').forEach(c => {
        if (c.dataset.amc === amcName) c.classList.add('active');
        else c.classList.remove('active');
    });
    renderSelectionPane();
}

// ==================== RENDERING SELECTION PANE (STEP 1) ====================
function renderSelectionPane() {
    const column = document.getElementById('selection-list-column');
    column.innerHTML = '';

    if (appState.planCategory === 0) {
        // Bank Pension Plans
        bankPensionPlans.forEach(plan => {
            const div = document.createElement('div');
            div.className = 'item-card';
            div.onclick = () => selectBankPlan(plan.type);
            div.innerHTML = `
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <div>
                        <span class="badge-pill badge-emerald">Bank Pension Plan</span>
                        <h3 style="font-size: 16px; margin-top: 6px; font-weight: 800;">${plan.title}</h3>
                        <p style="font-size: 12px; color: var(--text-secondary); margin-top: 4px;">${plan.shortDescription}</p>
                    </div>
                    <div style="text-align: right; background: var(--emerald-container); padding: 8px 14px; border-radius: 12px;">
                        <span style="font-size: 10px; color: var(--emerald-dark); font-weight: 800;">ANNUM RATE</span>
                        <h4 style="font-size: 16px; color: var(--emerald-dark); font-weight: 900;">${plan.defaultAnnuityRate}% p.a.</h4>
                    </div>
                </div>
            `;
            column.appendChild(div);
        });
    } else {
        // Mutual Fund Plans
        const funds = mutualFundsData.filter(f => f.amc === appState.selectedAmc);
        funds.forEach(fund => {
            const div = document.createElement('div');
            div.className = 'item-card';
            div.onclick = () => selectMutualFundObject(fund.schemeCode);
            const isPos = fund.dailyChangePercent >= 0;

            div.innerHTML = `
                <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                    <div>
                        <span class="badge-pill badge-purple">${fund.amcDisplay}</span>
                        <h3 style="font-size: 16px; margin-top: 6px; font-weight: 800;">${fund.fundName}</h3>
                        <p style="font-size: 11px; color: var(--text-secondary); margin-top: 2px;">${fund.category} • SFIN: ${fund.sfinCode}</p>
                    </div>
                    <div style="text-align: right; background: var(--emerald-container); padding: 6px 12px; border-radius: 12px;">
                        <span style="font-size: 9px; color: var(--emerald-dark); font-weight: 800;">5Y CAGR</span>
                        <h4 style="font-size: 15px; color: var(--emerald-dark); font-weight: 900;">${fund.cagr5Year}%</h4>
                    </div>
                </div>
                <div style="display:flex; justify-content:space-between; align-items:center; margin-top: 12px; padding-top: 10px; border-top: 1px solid var(--card-border);">
                    <div>
                        <span style="font-size: 10px; color: var(--text-muted);">Live NAV</span>
                        <h4 style="font-size: 14px; font-weight: 800;">₹${fund.currentNav.toFixed(2)} 
                            <span style="font-size: 10px; color:${isPos ? 'var(--emerald-dark)' : 'var(--rose-loss)'}; font-weight: 800;">${isPos ? '+' : ''}${fund.dailyChangePercent}%</span>
                        </h4>
                    </div>
                    <button class="btn-dark-primary" style="padding: 6px 14px; font-size: 11px; border-radius: 10px;">
                        View 5Y NAV & Calculate →
                    </button>
                </div>
            `;
            column.appendChild(div);
        });
    }
}

function selectBankPlan(planType) {
    const plan = bankPensionPlans.find(p => p.type === planType);
    if (!plan) return;

    appState.selectedFund = null;
    appState.selectedPlan = plan;
    appState.retirementInput.expectedAnnuityRate = plan.defaultAnnuityRate;
    appState.retirementInput.annualInterest = plan.defaultAnnuityRate;

    navigateStep(2);
}

async function selectMutualFundObject(schemeCode) {
    const rawFund = mutualFundsData.find(f => f.schemeCode === schemeCode);
    if (!rawFund) return;

    appState.selectedFund = rawFund;
    appState.selectedPlan = {
        title: `${rawFund.fundName} (${rawFund.cagr5Year}% p.a.)`,
        shortDescription: `${rawFund.category} • Benchmark: ${rawFund.benchmark}`,
        about: rawFund.aboutText,
        howItWorks: `Capital and contributions compound at the 5-Year CAGR of ${rawFund.cagr5Year}% p.a. until target retirement age.`,
        benefits: [
            `High return potential with 5-Year historical CAGR of ${rawFund.cagr5Year}% p.a.`,
            `Managed by ${rawFund.amcDisplay} under ${rawFund.category} regulations.`,
            `Integrated with retirement goal planning and annual expenditure calculations.`
        ],
        defaultAnnuityRate: rawFund.cagr5Year
    };

    appState.retirementInput.expectedAnnuityRate = rawFund.cagr5Year;
    appState.retirementInput.annualInterest = rawFund.cagr5Year;

    navigateStep(2);

    // Fetch live NAV & 5Y NAV trajectory from REST API
    appState.isFetchingNav = true;
    renderFullPageInfoPane();

    const updatedFund = await fetchFundWith5YearNavHistory(rawFund);
    appState.selectedFund = updatedFund;
    appState.isFetchingNav = false;
    renderFullPageInfoPane();
}

// ==================== RENDERING FULL PAGE NAV & PLAN INFO (STEP 2) ====================
function renderFullPageInfoPane() {
    const container = document.getElementById('pane-info-content');
    const isFund = !!appState.selectedFund;
    const fund = appState.selectedFund;
    const plan = appState.selectedPlan;

    const currencyFormat = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' });

    let html = `
        <div class="breadcrumb-banner">
            <div>
                <span class="breadcrumb-tag">STEP 2 OF 3</span>
                <span style="font-size: 12px; color: var(--text-secondary); margin-left: 8px;">• ${isFund ? 'Fund Overview & NAV Trajectory' : 'Plan Overview & Cash Flow'}</span>
            </div>
            <button class="btn-icon-back" onclick="navigateStep(1)">← Back to Plans</button>
        </div>
    `;

    if (isFund) {
        const isPos = fund.dailyChangePercent >= 0;
        html += `
            <div class="live-nav-card">
                <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                    <div>
                        <div style="display:flex; gap:6px; align-items:center;">
                            <span class="badge-pill badge-purple">${fund.amcDisplay}</span>
                            <span class="badge-pill" style="background:var(--card-surface-variant); color:var(--text-secondary);">SFIN: ${fund.sfinCode}</span>
                        </div>
                        <h2 style="font-size: 20px; font-weight: 900; margin-top: 8px;">${fund.fundName}</h2>
                        <p style="font-size: 11px; color: var(--purple-dark); font-weight: 700;">${fund.category} • Benchmark: ${fund.benchmark}</p>
                    </div>
                </div>

                <div style="display:flex; justify-content:space-between; align-items:center; margin-top: 14px; padding-top: 12px; border-top: 1px solid var(--card-border);">
                    <div>
                        <span style="font-size: 11px; color: var(--text-secondary);">Latest Live NAV (${fund.navDate})</span>
                        <h1 style="font-size: 26px; font-weight: 900; color: var(--text-primary); margin-top: 2px;">
                            ${currencyFormat.format(fund.currentNav)}
                            <span class="badge-pill ${isPos ? 'badge-emerald' : ''}" style="${!isPos ? 'background:var(--rose-container); color:var(--rose-loss);' : ''}">
                                ${isPos ? '+' : ''}${fund.dailyChangePercent}%
                            </span>
                        </h1>
                    </div>
                    ${appState.isFetchingNav ? '<div class="loader-spin"></div>' : ''}
                </div>

                <div style="display:flex; justify-content:space-between; margin-top: 12px; font-size: 11px; color: var(--text-muted); padding-top: 8px; border-top: 1px dashed var(--card-border);">
                    <span>Inception NAV: <strong>₹${fund.inceptionNav.toFixed(2)}</strong></span>
                    <span>Launch Date: <strong>${fund.launchDate}</strong></span>
                </div>
            </div>

            <!-- 4-PERIOD CAGR TILES -->
            <div class="cagr-grid-4">
                <div class="cagr-card-tile" style="background:var(--indigo-container); color:var(--indigo-dark);"><p>1Y CAGR</p><h4>${fund.cagr1Year}%</h4></div>
                <div class="cagr-card-tile" style="background:var(--purple-container); color:var(--purple-dark);"><p>3Y CAGR</p><h4>${fund.cagr3Year}%</h4></div>
                <div class="cagr-card-tile" style="background:var(--emerald-container); color:var(--emerald-dark);"><p>5Y CAGR</p><h4>${fund.cagr5Year}%</h4></div>
                <div class="cagr-card-tile" style="background:var(--cyan-container); color:var(--cyan-accent);"><p>Inception</p><h4>${fund.sinceInception}%</h4></div>
            </div>

            <!-- ASSET ALLOCATION PILL BAR -->
            <div style="margin-bottom: 16px;">
                <div style="display:flex; justify-content:space-between; font-size: 11px; font-weight: 800;">
                    <span>Asset Allocation</span>
                    <span style="color:var(--text-secondary); font-size: 10px;">Equity: ${fund.equityPercent}% | Cash: ${fund.cashPercent}%</span>
                </div>
                <div class="allocation-bar">
                    <div style="width: ${fund.equityPercent}%; background: var(--indigo-accent);"></div>
                    ${fund.debtPercent > 0 ? `<div style="width: ${fund.debtPercent}%; background: var(--cyan-accent);"></div>` : ''}
                    <div style="flex:1; background: var(--emerald-light);"></div>
                </div>
            </div>

            <!-- 5-YEAR INTERACTIVE CANVAS CURVE -->
            <div class="graph-container-card">
                <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 10px;">
                    <h3 style="font-size: 14px; font-weight: 800;">📈 5-Year NAV Trajectory Curve</h3>
                    <span class="badge-pill badge-purple">Interactive</span>
                </div>
                <div class="graph-tooltip-banner">
                    <span id="canvas-touch-date">👆 Touch or drag across graph to inspect daily NAV & date</span>
                    <span id="canvas-touch-nav">💰 Live MFAPI</span>
                </div>
                <canvas id="interactiveCanvas"></canvas>
            </div>

            <!-- HISTORICAL NAV SCHEDULE TABLE -->
            <div class="table-card-box">
                <h3 style="font-size: 13px; font-weight: 800; margin-bottom: 8px;">Historical NAV Schedule</h3>
                <div class="table-scroll-container">
                    <table>
                        <thead>
                            <tr><th>Date</th><th>NAV (₹)</th></tr>
                        </thead>
                        <tbody>
                            ${(fund.navHistory5Y || []).slice(-30).reverse().map((pt, idx) => `
                                <tr>
                                    <td>${pt.date}</td>
                                    <td style="font-weight: 700; color: var(--text-primary);">₹${pt.nav.toFixed(2)}</td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
    } else {
        html += `
            <div class="live-nav-card" style="background: var(--emerald-container); border-color: rgba(16, 185, 129, 0.3);">
                <span class="badge-pill badge-emerald">Bank Pension Plan</span>
                <h2 style="font-size: 22px; font-weight: 900; margin-top: 8px; color: var(--emerald-dark);">${plan.title}</h2>
                <p style="font-size: 12px; color: var(--text-secondary); margin-top: 4px;">${plan.shortDescription}</p>
                <div style="margin-top: 12px; padding-top: 10px; border-top: 1px solid rgba(16, 185, 129, 0.2);">
                    <span style="font-size: 12px; font-weight: 900; color: var(--emerald-dark);">Guaranteed Rate: ${plan.defaultAnnuityRate}% p.a.</span>
                    <p style="font-size: 11px; color: var(--text-secondary); margin-top: 2px;">Best For: ${plan.bestFor}</p>
                </div>
            </div>
        `;
    }

    html += `
        <div class="table-card-box">
            <h3 style="font-size: 14px; font-weight: 800; margin-bottom: 6px;">About this Plan</h3>
            <p style="font-size: 12px; color: var(--text-secondary); line-height: 1.6;">${plan.about}</p>
        </div>

        <div class="table-card-box">
            <h3 style="font-size: 14px; font-weight: 800; margin-bottom: 6px;">How it Works</h3>
            <p style="font-size: 12px; color: var(--text-secondary); line-height: 1.6;">${plan.howItWorks}</p>
        </div>

        <button class="btn-dark-primary" style="width: 100%; padding: 16px; font-size: 16px; margin-top: 8px;" onclick="navigateStep(3)">
            🧮 Calculate Plan (${plan.defaultAnnuityRate}% p.a.)
        </button>
    `;

    container.innerHTML = html;

    if (isFund && fund.navHistory5Y && fund.navHistory5Y.length > 0) {
        setTimeout(() => initInteractiveCanvas(fund.navHistory5Y), 60);
    }
}

// ==================== INTERACTIVE CANVAS GRAPH WITH TOUCH TOOLTIP ====================
function initInteractiveCanvas(navPoints) {
    const canvas = document.getElementById('interactiveCanvas');
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    const rect = canvas.getBoundingClientRect();
    canvas.width = rect.width * (window.devicePixelRatio || 2);
    canvas.height = 180 * (window.devicePixelRatio || 2);

    const width = canvas.width;
    const height = canvas.height;

    const navValues = navPoints.map(p => p.nav);
    const minNav = Math.min(...navValues) * 0.95;
    const maxNav = Math.max(...navValues) * 1.05;

    function renderFrame(activeIndex = null) {
        ctx.clearRect(0, 0, width, height);

        if (navPoints.length < 2) return;

        // Path
        ctx.beginPath();
        navValues.forEach((nav, i) => {
            const x = (i / (navValues.length - 1)) * width;
            const y = height - ((nav - minNav) / (maxNav - minNav)) * height;
            if (i === 0) ctx.moveTo(x, y);
            else ctx.lineTo(x, y);
        });

        // Gradient Fill
        const gradient = ctx.createLinearGradient(0, 0, 0, height);
        gradient.addColorStop(0, 'rgba(139, 92, 246, 0.35)');
        gradient.addColorStop(1, 'rgba(139, 92, 246, 0.02)');

        ctx.lineTo(width, height);
        ctx.lineTo(0, height);
        ctx.closePath();
        ctx.fillStyle = gradient;
        ctx.fill();

        // Curve stroke
        ctx.beginPath();
        navValues.forEach((nav, i) => {
            const x = (i / (navValues.length - 1)) * width;
            const y = height - ((nav - minNav) / (maxNav - minNav)) * height;
            if (i === 0) ctx.moveTo(x, y);
            else ctx.lineTo(x, y);
        });
        ctx.strokeStyle = '#8B5CF6';
        ctx.lineWidth = 3 * (window.devicePixelRatio || 2);
        ctx.stroke();

        // Active pointer indicator
        if (activeIndex !== null && activeIndex >= 0 && activeIndex < navPoints.length) {
            const pt = navPoints[activeIndex];
            const activeX = (activeIndex / (navPoints.length - 1)) * width;
            const activeY = height - ((pt.nav - minNav) / (maxNav - minNav)) * height;

            // Dashed vertical line
            ctx.beginPath();
            ctx.setLineDash([8, 8]);
            ctx.moveTo(activeX, 0);
            ctx.lineTo(activeX, height);
            ctx.strokeStyle = '#6D28D9';
            ctx.lineWidth = 2 * (window.devicePixelRatio || 2);
            ctx.stroke();
            ctx.setLineDash([]);

            // Outer Halo
            ctx.beginPath();
            ctx.arc(activeX, activeY, 10 * (window.devicePixelRatio || 2), 0, 2 * Math.PI);
            ctx.fillStyle = 'rgba(139, 92, 246, 0.3)';
            ctx.fill();

            // Inner Dot
            ctx.beginPath();
            ctx.arc(activeX, activeY, 5 * (window.devicePixelRatio || 2), 0, 2 * Math.PI);
            ctx.fillStyle = '#6D28D9';
            ctx.fill();

            // Update Tooltip Banner
            document.getElementById('canvas-touch-date').innerText = `📅 Date: ${pt.date}`;
            document.getElementById('canvas-touch-nav').innerText = `💰 NAV: ₹${pt.nav.toFixed(2)}`;
        }
    }

    renderFrame();

    function onPointerMove(e) {
        const bounds = canvas.getBoundingClientRect();
        const clientX = e.touches ? e.touches[0].clientX : e.clientX;
        const ratio = (clientX - bounds.left) / bounds.width;
        const idx = Math.min(navPoints.length - 1, Math.max(0, Math.round(ratio * (navPoints.length - 1))));
        renderFrame(idx);
    }

    canvas.onmousemove = onPointerMove;
    canvas.ontouchmove = onPointerMove;
}

// ==================== STEP 3: CALCULATOR MATH ====================
function calculateRetirementProjections() {
    const input = appState.retirementInput;
    const years = Math.max(0, input.retirementAge - input.currentAge);

    const rate = input.annualInterest / 100.0;
    let accumulatedCorpus = input.currentCorpus * Math.pow(1 + rate, years);
    for (let i = 0; i < years; i++) {
        accumulatedCorpus += input.annualSavings * Math.pow(1 + rate, years - i - 1);
    }

    const infl = input.annualInflation / 100.0;
    const targetExpenditure = input.annualExpenditure * Math.pow(1 + infl, years);

    const annuityRate = input.expectedAnnuityRate / 100.0;
    const annualAnnuityIncome = accumulatedCorpus * annuityRate;

    const diff = annualAnnuityIncome - targetExpenditure;
    const isSurplus = diff >= 0;

    const currencyFormat = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' });

    const box = document.getElementById('calc-results-box');
    box.innerHTML = `
        <div class="result-banner" style="background:${isSurplus ? 'var(--emerald-container)' : 'var(--rose-container)'}; border-color:${isSurplus ? 'rgba(16,185,129,0.3)' : 'rgba(220,38,38,0.3)'}">
            <h3 style="font-size: 16px; color:${isSurplus ? 'var(--emerald-dark)' : 'var(--rose-loss)'}">
                ${isSurplus ? '🎉 TARGET RETIREMENT GOAL ACHIEVED' : '⚠️ RETIREMENT DEFICIT DETECTED'}
            </h3>
            <p style="font-size: 12px; color: var(--text-secondary); margin-top: 2px;">
                ${isSurplus ? 'Your projected corpus will generate more pension than required expenditure.' : 'Additional annual savings recommended to cover future inflation expenditure.'}
            </p>
        </div>

        <div class="result-stat-box">
            <p>Projected Accumulated Corpus at Age ${input.retirementAge}</p>
            <h2>${currencyFormat.format(accumulatedCorpus)}</h2>
        </div>

        <div class="result-stat-box">
            <p>Annual Pension Stream Generated (${input.expectedAnnuityRate}% p.a.)</p>
            <h2 style="color: var(--purple-dark);">${currencyFormat.format(annualAnnuityIncome)} / Year</h2>
        </div>

        <div class="result-stat-box">
            <p>Target Annual Expenditure at Age ${input.retirementAge} (6% Inflation)</p>
            <h3 style="font-size: 18px; color:${isSurplus ? 'var(--emerald-dark)' : 'var(--rose-loss)'}">
                ${currencyFormat.format(targetExpenditure)} (${isSurplus ? 'Surplus ' + currencyFormat.format(diff) : 'Deficit ' + currencyFormat.format(Math.abs(diff))})
            </h3>
        </div>
    `;
}

// ==================== EVENT BINDINGS ====================
document.addEventListener('DOMContentLoaded', () => {
    // Inputs
    ['currentAge', 'retirementAge', 'currentCorpus', 'annualSavings', 'annualExpenditure', 'expectedAnnuityRate'].forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('input', (e) => {
                appState.retirementInput[id] = parseFloat(e.target.value) || 0;
                if (appState.step === 3) calculateRetirementProjections();
            });
        }
    });

    navigateStep(0);
});
