import { useEffect, useRef } from "react";
import { MarketDepthRow } from "./useMarketDepthData";
import { PriceCell } from "./PriceCell";
import { QuantityCell } from "./QuantityCell";

interface MarketDepthPanelProps {
    data: MarketDepthRow[];
}

export const MarketDepthPanel = ({ data }: MarketDepthPanelProps) => {
    const previousDataRef = useRef<MarketDepthRow[] | null>(null);  // Use useRef to store previous data

    useEffect(() => {
        // Update the ref to store the current data
        previousDataRef.current = data;
    }, [data]);

    return (
        <div className="table-container">
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
                {data.map((row) => {
                    // Compare the current row with the previous row based on symbolLevel
                    const previousRow = previousDataRef.current?.find(prev => prev.symbolLevel === row.symbolLevel);
                    return (
                        <tr key={row.symbolLevel}>
                            <td>{row.level}</td>
                            <QuantityCell
                                quantity={row.bidQuantity}
                                previousQuantity={previousRow ? previousRow.bidQuantity : null} type={"bid"}                            />
                            <PriceCell
                                price={row.bid}
                                previousPrice={previousRow ? previousRow.bid : null}
                            />
                            <PriceCell
                                price={row.offer}
                                previousPrice={previousRow ? previousRow.offer : null}
                            />
                            <QuantityCell
                                quantity={row.offerQuantity}
                                previousQuantity={previousRow ? previousRow.offerQuantity : null} type={"ask"}                            />
                        </tr>
                    );
                })}
            </tbody>
        </table>
    </div>
    );
};
