// import { ReactNode, useEffect, useState } from "react";
// import { MarketDepthRow } from "./useMarketDepthData";
// import { MarketDepthPanel } from "./MarketDepthPanel";
import "../market-depth/MarketDepthCSS.css";

interface PriceCellProps {
    price: number; // The current price
    previousPrice: number | null; // Previous price to compare with
}

export const PriceCell = ({price, previousPrice} : PriceCellProps) => {

    const getPriceArrow = () => {
        if (previousPrice === null) return null; // No arrow for the first render
        if (price > previousPrice) return <span className="arrowUp">⬆</span>; // Price increased
        if (price < previousPrice) return <span className="arrowDown">⬇</span>; // Price decreased
        return "➖"; // No change
    };

    // // Colour changes based on price increase or decrease
    // const getPriceChange = () => {
    //     if (previousPrice === null) return ''; // No class for first render
    //     if (price > previousPrice) return 'priceIncrease'; // Green 
    //     if (price < previousPrice) return 'priceDecrease'; // Red
    //     return ''; // No change
    // }

    return (
        // <td className={getPriceChange()}>
        <td>
            {price} <span className="arrow">{getPriceArrow()}</span>
        </td>
    );
};
