import { MarketDepthRow } from "./useMarketDepthData";
import { PriceCell } from "./PriceCell";
import { useState, useEffect } from "react";
import { QuantityCell } from "./QuantityCell";


interface MarketDepthPanelProps {
    data: MarketDepthRow[];
}

export const MarketDepthPanel = ({data}: MarketDepthPanelProps) => {
    const [previousData, setPreviousData] = useState<MarketDepthRow[] | null>(null);

    useEffect(() => {
        // On every data update, set the current data as the previous data for comparison
        setPreviousData(data);
    }, [data]);
    return (
        <table>
            <thead>
                <tr>
                    <th></th>
                    <th colSpan={2}>Bid</th>
                    <th colSpan={2}>Ask</th>
                </tr>
                <tr>
                    <th></th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Price</th>
                    <th>Quantity</th>
                </tr>
            </thead>
            <tbody>
                {data.map((row, index) => (
                    <tr key={row.symbolLevel}>
                    <td>{row.level}</td>
                    <td>{row.bidQuantity}</td>
                    <PriceCell
                        price={row.bid}
                        previousPrice={previousData ? previousData[index]?.bid : null}/>
                    <td>{row.offer}</td>
                    <QuantityCell
                        quantity={row.offerQuantity}
                        previousQuantity={previousData ? previousData[index]?.offerQuantity : null}
                    />   
                    </tr>
                ))}
            </tbody>
        </table>
    )
};