import {Day} from "./application/day";
import {useQuery} from "react-query";
import ApplicationService from "../../../services/ApplicationService";
import {Loading} from "../../snippets/Loading";
import {useState} from "react";
import {Container} from "react-bootstrap";
import {Time} from "./application/time";
import {ApplicationItem} from "./application/applicationItem";
import DateHelper from "../../../helpers/DateHelper";

export const ApplicationList = () => {

    const [start, setStart] = useState()
    const [end, setEnd] = useState()
    const [build, setBuild] = useState("")
    const [room, setRoom] = useState("")


    const {
        data,
        isLoading,
        error
    } = useQuery(['applications'], (params) => ApplicationService.getApplications({
        start: params.start,
        end: params.end,
        buildId: params.buildId,
        roomId: params.roomId
    }), {
        select: ({data}) => data,
        refetchOnWindowFocus: false,
        keepPreviousData: true
    })

    if (isLoading) {
        return (
            <Loading/>
        )
    }

    if (!data.length){
        return(
            <Container className='text-center text-danger fs-1 fw-bold mt-5'>Нет заявок</Container>
        )
    }
    return (
        <div className='mt-5'>
            {data.map((item) => (
                <ApplicationItem name={item.owner_name}
                                 email={item.owner_email}
                                 build={item.build_id}
                                 room={item.room_id}
                                 start={DateHelper.normalizeDate(item.start_time)}
                                 end={DateHelper.normalizeDate(item.end_time)}
                                 dublicate={item.is_duplicate}
                                 dublicate_end={item.end_time_duplicate}
                                 key={item.application_id}
                                 id={item.application_id}
                />

            ))}
        </div>
    )
}